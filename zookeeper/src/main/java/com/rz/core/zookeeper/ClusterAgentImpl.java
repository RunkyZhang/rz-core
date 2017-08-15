package com.rz.core.zookeeper;

import com.alibaba.fastjson.JSON;
import com.rz.core.Assert;
import com.rz.core.Tuple2;
import com.rz.core.RZHelper;
import com.rz.core.zookeeper.event.ItemsChangedEvent;
import com.rz.core.zookeeper.event.MasterChangedEvent;
import com.rz.core.zookeeper.event.ZooKeeperEvent;
import com.rz.core.zookeeper.listener.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class ClusterAgentImpl implements ClusterAgent {
    private final static Charset charset = Charset.forName("UTF-8");
    private final static Object lock = new Object();
    private static Map<String, ClusterLock> clusterLocks = new ConcurrentHashMap<>();
    private static Queue<String> dirtyClusterLockPaths = new ConcurrentLinkedQueue<>();

    private String rootPath;
    private String applicationId;
    private String applicationVersion;
    private String ip;
    private String lockValue;
    private ZooKeeperMaintainer zooKeeperMaintainer;
    private ClusterItem current;
    private ClusterItem master;
    private List<ClusterItem> clusterItems;
    private boolean isConnected;

    private ItemsChangedListener itemsChangedListener;
    private MasterChangedListener masterChangedListener;
    private ZooKeeperListener<ZooKeeperEvent> connectedListener;
    private ZooKeeperListener<ZooKeeperEvent> disconnectedListener;

    public ClusterAgentImpl(
            String connectString,
            String rootPath,
            String applicationId,
            String applicationVersion) throws IOException {
        Assert.isNotBlank(connectString, "connectString");
        Assert.isNotBlank(applicationId, "applicationId");
        Assert.isNotBlank(applicationVersion, "applicationVersion");

        this.rootPath = this.formatRootPath(rootPath);
        this.applicationId = applicationId;
        this.applicationVersion = applicationVersion;
        this.ip = RZHelper.getIpV4();
        this.lockValue = this.ip + "_" + String.valueOf(RZHelper.getCurrentProcessId());
        this.current = new ClusterItem();
        this.current.setIp(this.ip);
        this.current.setCreatedTime(new Date().getTime());
        this.clusterItems = new ArrayList<>();
        this.clusterItems.add(this.current);
        this.isConnected = false;

        this.zooKeeperMaintainer = new ZooKeeperMaintainer(
                connectString,
                40 * 1000,
                o -> {
                    try {
                        this.processWatch(o);
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                },
                o -> {
                    try {
                        this.initExpiredConnection(o);
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                });
    }

    private String formatRootPath(String rootPath) {
        if (StringUtils.isBlank(rootPath)) {
            return "/defaultRoot";
        }

        rootPath = rootPath.startsWith("/") ? rootPath : "/" + rootPath;
        rootPath = rootPath.endsWith("/") ? rootPath.substring(0, rootPath.length() - 1) : rootPath;

        return rootPath;
    }

    private void processWatch(Tuple2<WatchedEvent, ZooKeeper> tuple) throws KeeperException, InterruptedException {
        WatchedEvent watchedEvent = tuple.getItem1();
        ZooKeeper zooKeeper = tuple.getItem2();

        int queueCount;
        if (Watcher.Event.KeeperState.Disconnected != watchedEvent.getState()
                && 0 < (queueCount = ClusterAgentImpl.dirtyClusterLockPaths.size())) {
            for (int i = 0; i < queueCount; i++) {
                String lockPath = ClusterAgentImpl.dirtyClusterLockPaths.poll();
                if (null == lockPath) {
                    break;
                } else {
                    this.releaseLock(zooKeeper, lockPath);
                }
            }
        }

        if (Watcher.Event.EventType.None == watchedEvent.getType()
                && Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            this.isConnected = true;
            if (null != this.connectedListener) {
                try {
                    this.connectedListener.onZooKeeperEvent(this, ZooKeeperEvent.Empty);
                } catch (Throwable throwable) {
                    System.out.println("Failed to invoke event [Connected].");
                    // log
                }
            }

            String path = this.resolvePath(PathTypeEnum.VERSION);
            zooKeeper.getChildren(path, true);

//            if (false == this._basicApplicationVersionZNodePathFlag && null != zooKeeper.Exists(zNodePath, false)) {
//                zooKeeper.GetChildren(zNodePath, true);
//                this._basicApplicationVersionZNodePathFlag = true;
//            } else {
//                zooKeeper.GetChildren(zNodePath, true);
//            }
        } else if (Watcher.Event.EventType.None == watchedEvent.getType()
                && Watcher.Event.KeeperState.Disconnected == watchedEvent.getState()) {
            this.isConnected = false;
            if (null != this.disconnectedListener) {
                try {
                    this.disconnectedListener.onZooKeeperEvent(this, ZooKeeperEvent.Empty);
                } catch (Exception exception) {
                    System.out.println("Failed to invoke event [Disconnected].");
                }
            }
        } else if (Watcher.Event.EventType.NodeDataChanged == watchedEvent.getType()) {

        } else if (Watcher.Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {
            this.fireItemsChangedEvent(zooKeeper);
            this.fireMasterChangedEvent(zooKeeper);
        } else if (Watcher.Event.EventType.NodeCreated == watchedEvent.getType()) {
            String path = watchedEvent.getPath();
            if (path.startsWith(this.resolvePath(PathTypeEnum.LOCK))) {
                if (ClusterAgentImpl.clusterLocks.containsKey(path)) {
                    try {
                        byte[] bytes = zooKeeper.getData(path, false, new Stat());
                        if (null == bytes) {
                            return;
                        }
                        String value = new String(bytes, ClusterAgentImpl.charset);
                        if (!this.lockValue.equals(value)) {
                            zooKeeper.exists(path, true);
                        }
                    } catch (KeeperException.NoNodeException noNodeException) {
                        // ignore
                        zooKeeper.exists(path, true);
                    }
                }
            }
        } else if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
            String path = watchedEvent.getPath();
            if (ClusterAgentImpl.clusterLocks.containsKey(path)) {
                ClusterLock clusterLock = ClusterAgentImpl.clusterLocks.get(path);
                if (null == clusterLock || null == clusterLock.getSemaphore()) {
                    return;
                }

                clusterLock.getSemaphore().release();
            }
        }
    }

    private void initExpiredConnection(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // Root
        String now = String.valueOf(System.currentTimeMillis());
        this.createNode(zooKeeper, this.resolvePath(PathTypeEnum.ROOT), now);
        this.createNode(zooKeeper, this.resolvePath(PathTypeEnum.CLUSTER), now);
        this.createNode(zooKeeper, this.resolvePath(PathTypeEnum.LOCK), now);
        this.createNode(zooKeeper, this.resolvePath(PathTypeEnum.APPLICATION), now);
        this.createNode(zooKeeper, this.resolvePath(PathTypeEnum.VERSION), now);

        // wait for create instance and zooKeeper fire event
        zooKeeper.getChildren(this.resolvePath(PathTypeEnum.VERSION), true);

        // Item
        String json = JSON.toJSONString(this.current);
        String path = zooKeeper.create(
                this.resolvePath(PathTypeEnum.ITEM),
                json.getBytes(ClusterAgentImpl.charset),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        this.current.setPath(path);
    }

    @Override
    public void listenItemsChanged(ItemsChangedListener listener) {
        Assert.isNotNull(listener, "listener");

        this.itemsChangedListener = listener;
    }

    @Override
    public void listenMasterChanged(MasterChangedListener listener) {
        Assert.isNotNull(listener, "listener");

        this.masterChangedListener = listener;
    }

    @Override
    public void listenConnected(ZooKeeperListener listener) {
        Assert.isNotNull(listener, "listener");

        this.connectedListener = listener;
    }

    @Override
    public void listenDisconnected(ZooKeeperListener listener) {
        Assert.isNotNull(listener, "listener");

        this.disconnectedListener = listener;
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public ClusterItem getCurrent() {
        return this.current;
    }

    @Override
    public ClusterItem getMaster() {
        return null;
    }

    @Override
    public List<ClusterItem> getClusterItems() {
        return this.clusterItems;
    }

    @Override
    public void acquireLock(String lockName) throws KeeperException, InterruptedException {
        acquireLock(lockName, Integer.MAX_VALUE);
    }

    @Override
    public void acquireLock(String lockName, int timeout) throws KeeperException, InterruptedException {
        Assert.isNotBlank(lockName, "lockName");
        if (0 >= timeout) {
            return;
        }

        String path = this.resolvePath(PathTypeEnum.LOCK) + "/" + lockName;

        if (!ClusterAgentImpl.clusterLocks.containsKey(path)) {
            synchronized (ClusterAgentImpl.lock) {
                if (!ClusterAgentImpl.clusterLocks.containsKey(path)) {
                    ClusterLock clusterLock = new ClusterLock();
                    clusterLock.setSemaphore(new Semaphore(1));
                    ClusterAgentImpl.clusterLocks.put(path, clusterLock);
                }
            }
        }
        ClusterLock clusterLock = ClusterAgentImpl.clusterLocks.get(path);

        Stat state = this.zooKeeperMaintainer.getZooKeeper().exists(path, true);
        if (null == state) {
            try {
                this.zooKeeperMaintainer.getZooKeeper().create(
                        path,
                        this.lockValue.getBytes(ClusterAgentImpl.charset),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                clusterLock.setCreatedTime(System.currentTimeMillis());

                return;
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                byte[] bytes = this.zooKeeperMaintainer.getZooKeeper().getData(path, false, new Stat());
                if (null != bytes) {
                    String value = new String(bytes, ClusterAgentImpl.charset);
                    if (this.lockValue.equals(value)) {
                        return;
                    }
                }
            }
        }
        this.zooKeeperMaintainer.getZooKeeper().exists(path, true);

        clusterLock.getSemaphore().acquire(timeout);

        this.zooKeeperMaintainer.getZooKeeper().create(
                path,
                this.lockValue.getBytes(ClusterAgentImpl.charset),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        clusterLock.setCreatedTime(System.currentTimeMillis());
    }

    @Override
    public void releaseLock(String lockName) {
        this.releaseLock(this.zooKeeperMaintainer.getZooKeeper(), lockName);
    }

    @Override
    public ZooKeeperMaintainer getZooKeeperMaintainer() {
        return this.zooKeeperMaintainer;
    }

    private void releaseLock(ZooKeeper zooKeeper, String lockPath) {
        if (StringUtils.isBlank(lockPath)) {
            return;
        }

        if (ClusterAgentImpl.clusterLocks.containsKey(lockPath)) {
            ClusterLock clusterLock = ClusterAgentImpl.clusterLocks.get(lockPath);
            if (null == clusterLock) {
                return;
            }

            try {
                Stat state = zooKeeper.exists(lockPath, false);

                if (null != state) {
                    byte[] bytes = zooKeeper.getData(lockPath, false, new Stat());
                    if (null == bytes) {
                        return;
                    }

                    String value = new String(bytes, ClusterAgentImpl.charset);
                    if (this.lockValue.equals(value)) {
                        zooKeeper.delete(lockPath, -1);
                    }
                }
            } catch (KeeperException.NoNodeException noNodeException) {
                // ignore
            } catch (Throwable throwable) {
                ClusterAgentImpl.dirtyClusterLockPaths.add(lockPath);

                // log
                System.out.println(String.format("Failed to release cluster lock(%s).", lockPath));
            }
        }
    }

    private String resolvePath(PathTypeEnum pathType) {
        String path = null;
        if (PathTypeEnum.ROOT == pathType) {
            path = this.rootPath;
        } else if (PathTypeEnum.CLUSTER == pathType) {
            path = String.format(
                    "%s/%s",
                    this.rootPath,
                    PathTypeEnum.CLUSTER.name());
        } else if (PathTypeEnum.LOCK == pathType) {
            path = String.format(
                    "%s/%s",
                    this.rootPath,
                    PathTypeEnum.LOCK.name());
        } else if (PathTypeEnum.APPLICATION == pathType) {
            path = String.format(
                    "%s/%s/%s",
                    this.rootPath,
                    PathTypeEnum.CLUSTER.name(),
                    this.applicationId);
        } else if (PathTypeEnum.VERSION == pathType) {
            path = String.format(
                    "%s/%s/%s/%s",
                    this.rootPath,
                    PathTypeEnum.CLUSTER.name(),
                    this.applicationId,
                    this.applicationVersion);
        } else if (PathTypeEnum.ITEM == pathType) {
            path = String.format(
                    "%s/%s/%s/%s/%s",
                    this.rootPath,
                    PathTypeEnum.CLUSTER.name(),
                    this.applicationId,
                    this.applicationVersion,
                    this.ip);
        }

        return path;
    }

    private void createNode(ZooKeeper zooKeeper, String path, String value) throws KeeperException, InterruptedException {
        value = null == value ? "" : value;
        Stat state = zooKeeper.exists(path, false);
        if (null == state) {
            try {
                zooKeeper.create(
                        path,
                        value.getBytes(ClusterAgentImpl.charset),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                // ignore it
            }
        }
    }

    private void fireItemsChangedEvent(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String versionPath = this.resolvePath(PathTypeEnum.VERSION);
        List<String> itemPaths = zooKeeper.getChildren(versionPath, true);

        if (RZHelper.isEmptyCollection(itemPaths)) {
            return;
        }
        final List<String> finalItemPaths = itemPaths
                .stream()
                .map(o -> versionPath + "/" + o)
                .collect(Collectors.toList());
        if (this.clusterItems.size() == itemPaths.size() &&
                this.clusterItems.stream().allMatch(i -> finalItemPaths.stream().anyMatch(p -> p.equals(i.getPath())))) {
            return;
        }

        List<ClusterItem> clusterItems = new ArrayList<>();
        for (String itemPath : itemPaths) {
            ClusterItem clusterItem;
            try {
                clusterItem = this.getClusterItem(zooKeeper, itemPath);
            } catch (Exception exception) {
                System.out.println(String.format("Failed to get cluster item with path(%s).", itemPath));
                // log
                continue;
            }
            if (null == clusterItem) {
                System.out.println(String.format("Failed to get cluster item with path(%s).", itemPath));
                // log
                continue;
            }

            clusterItems.add(clusterItem);
        }
        this.clusterItems = clusterItems;

        if (null != this.itemsChangedListener) {
            ItemsChangedEvent itemsChangedEvent = new ItemsChangedEvent(this.clusterItems);
            try {
                this.itemsChangedListener.onZooKeeperEvent(this, itemsChangedEvent);
            } catch (Exception exception) {
                System.out.println("Failed to invoke event [ItemsChanged].");
                // log
            }
        }
    }

    private void fireMasterChangedEvent(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        String versionPath = this.resolvePath(PathTypeEnum.VERSION);
        List<String> itemPaths = zooKeeper.getChildren(versionPath, true);

        if (RZHelper.isEmptyCollection(itemPaths)) {
            return;
        }

        String itemPath = versionPath + "/" + itemPaths.stream().sorted().findFirst().orElse("");
        if (null == this.master || !itemPath.equals(this.master.getPath())) {
            ClusterItem clusterItem = this.getClusterItem(zooKeeper, itemPath);
            if(null == clusterItem){
                System.out.println(String.format("Failed to get cluster item with path(%s).", itemPath));
                return;
            }

            this.master = clusterItem;
            if (null != this.masterChangedListener) {
                MasterChangedEvent masterChangedEvent = new MasterChangedEvent(this.master);
                try {
                    this.masterChangedListener.onZooKeeperEvent(this, masterChangedEvent);
                } catch (Throwable throwable) {
                    System.out.println(String.format("Failed to invoke event [MasterChanged].", itemPath));
                    // log
                }
            }
        }
    }

    private ClusterItem getClusterItem(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        byte[] bytes = zooKeeper.getData(path, false, new Stat());
        if (null == bytes) {
            return null;
        }
        String value = new String(bytes, ClusterAgentImpl.charset);
        ClusterItem clusterItem = JSON.parseObject(value, ClusterItem.class);
        clusterItem.setPath(path);

        return clusterItem;
    }
}
