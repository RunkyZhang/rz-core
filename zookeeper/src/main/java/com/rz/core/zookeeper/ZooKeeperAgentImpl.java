package com.rz.core.zookeeper;

import com.alibaba.fastjson.JSON;
import com.rz.core.Assert;
import com.rz.core.Tuple2;
import com.rz.core.RZHelper;
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

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class ZooKeeperAgentImpl implements ZooKeeperAgent {
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
    private ClusterItem clusterItem;
    private List<ClusterItem> clusterItems;
    private boolean isConnected;

    private NodeValueChangedListener nodeValueChangedListener;
    private NodeChildrenChangedListener nodeChildrenChangedListener;
    private ItemsChangedListener itemsChangedListener;
    private MasterChangedListener masterChangedListener;
    private ZooKeeperListener connectedListener;
    private ZooKeeperListener disconnectedListener;

    public ZooKeeperAgentImpl(
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
        this.clusterItem = new ClusterItem();
        this.clusterItem.setIp(this.ip);
        this.clusterItem.setCreatedTime(new Date().getTime());
        this.clusterItems = new ArrayList<>();
        this.clusterItems.add(this.clusterItem);
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

    private void processWatch(Tuple2<WatchedEvent, ZooKeeper> tuple) {
        WatchedEvent watchedEvent = tuple.getItem1();
        ZooKeeper zooKeeper = tuple.getItem2();

        int queueCount;
        if (Watcher.Event.KeeperState.Disconnected != watchedEvent.getState()
                && 0 < (queueCount = ZooKeeperAgentImpl.dirtyClusterLockPaths.size())) {
            for (int i = 0; i < queueCount; i++) {
                String lockPath = ZooKeeperAgentImpl.dirtyClusterLockPaths.poll();
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
//            this.FireConfigFileChangedEvent(zooKeeper);
        } else if (Watcher.Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {

//            this.FireInstancesChangedEvent(zooKeeper);
//            this.FireMsaterChangedEvent(zooKeeper);

        } else if (Watcher.Event.EventType.NodeCreated == watchedEvent.getType()) {
            String path = watchedEvent.getPath();
            if (path.startsWith(this.resolvePath(PathTypeEnum.LOCK))) {
                if (ZooKeeperAgentImpl.clusterLocks.containsKey(path)) {
                    try {
                        byte[] bytes = zooKeeper.getData(path, false, new Stat());
                        if (null == bytes) {
                            return;
                        }
                        String value = new String(bytes, ZooKeeperAgentImpl.charset);
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
            if (ZooKeeperAgentImpl.clusterLocks.containsKey(path)) {
                ClusterLock clusterLock = ZooKeeperAgentImpl.clusterLocks.get(path);
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
        String json = JSON.toJSONString(this.clusterItem);
        String path = zooKeeper.create(
                this.resolvePath(PathTypeEnum.ITEM),
                json.getBytes(ZooKeeperAgentImpl.charset),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        this.clusterItem.setPath(path);
    }

    @Override
    public void listenNodeValueChanged(String path, NodeValueChangedListener listener) {
        Assert.isNotBlank(path, "path");
        Assert.isNotNull(listener, "listener");

        this.nodeValueChangedListener = listener;
    }

    @Override
    public void listenNodeChildrenChanged(String path, NodeChildrenChangedListener listener) {
        Assert.isNotBlank(path, "path");
        Assert.isNotNull(listener, "listener");

        this.nodeChildrenChangedListener = listener;
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
        return this.clusterItem;
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
    public void acquireLock(String lockName) {

    }

    @Override
    public void acquireLock(String lockName, int timeout) throws KeeperException, InterruptedException {
        Assert.isNotBlank(lockName, "lockName");
        if (0 >= timeout) {
            return;
        }

        String path = this.resolvePath(PathTypeEnum.LOCK) + "/" + lockName;

        if (!ZooKeeperAgentImpl.clusterLocks.containsKey(path)) {
            synchronized (ZooKeeperAgentImpl.lock) {
                if (!ZooKeeperAgentImpl.clusterLocks.containsKey(path)) {
                    ClusterLock clusterLock = new ClusterLock();
                    clusterLock.setSemaphore(new Semaphore(1));
                    ZooKeeperAgentImpl.clusterLocks.put(path, clusterLock);
                }
            }
        }
        ClusterLock clusterLock = ZooKeeperAgentImpl.clusterLocks.get(path);

        Stat state = this.zooKeeperMaintainer.getZooKeeper().exists(path, true);
        if (null == state) {
            try {
                this.zooKeeperMaintainer.getZooKeeper().create(
                        path,
                        this.lockValue.getBytes(ZooKeeperAgentImpl.charset),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                clusterLock.setCreatedTime(System.currentTimeMillis());

                return;
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                byte[] bytes = this.zooKeeperMaintainer.getZooKeeper().getData(path, false, new Stat());
                if (null != bytes) {
                    String value = new String(bytes, ZooKeeperAgentImpl.charset);
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
                this.lockValue.getBytes(ZooKeeperAgentImpl.charset),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        clusterLock.setCreatedTime(System.currentTimeMillis());
    }

    @Override
    public void releaseLock(String lockName) {
        this.releaseLock(this.zooKeeperMaintainer.getZooKeeper(), lockName);
    }

    private void releaseLock(ZooKeeper zooKeeper, String lockPath) {
        if (StringUtils.isBlank(lockPath)) {
            return;
        }

        if (ZooKeeperAgentImpl.clusterLocks.containsKey(lockPath)) {
            ClusterLock clusterLock = ZooKeeperAgentImpl.clusterLocks.get(lockPath);
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

                    String value = new String(bytes, ZooKeeperAgentImpl.charset);
                    if (this.lockValue.equals(value)) {
                        zooKeeper.delete(lockPath, -1);
                    }
                }
            } catch (KeeperException.NoNodeException noNodeException) {
                // ignore
            } catch (Throwable throwable) {
                ZooKeeperAgentImpl.dirtyClusterLockPaths.add(lockPath);

                // log
                System.out.println(String.format("Failed to release cluster lock(%s).", lockPath));
            }
        }
    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

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
                        value.getBytes(ZooKeeperAgentImpl.charset),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                // ignore it
            }
        }
    }

//    private String getOrCreateZNodeValue(ZooKeeper zooKeeper, String zNodePath)
//            throws KeeperException, InterruptedException {
//        String value = null;
//        Stat state = zooKeeper.exists(zNodePath, false);
//        if (null == state) {
//            try {
//                value = String.valueOf(System.currentTimeMillis());
//                zooKeeper.create(zNodePath, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            } catch (KeeperException.NodeExistsException nodeExistsException) {
//                // ignore it
//            }
//        }
//        if (null == value) {
//            byte[] bytes = zooKeeper.getData(zNodePath, false, state);
//            if (null == bytes) {
//                value = null;
//            } else {
//                value = new String(bytes, ZooKeeperAgentImpl.charset);
//            }
//        }
//
//        return value;
//    }
}
