package com.rz.core.recipe.scheduling;

import java.util.Collections;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.InitializingBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperFactory implements InitializingBean {
    // public static Logger log = Logger.getLogger(ZookeeperFactory.class);

    /**
     * zookeeper服务地址
     */
    private String hosts;
    /**
     * 回话的超时时间(毫秒)
     */
    private Integer sessionTimeOut;
    /**
     * 连接的超时时间(毫秒)
     */
    private Integer connectionTimeOut;
    /**
     * 命名空间
     **/
    private String nameSpace;
    /**
     * zookeeper管理对象
     */
    private CuratorFramework zkTools;
    /**
     * 独享队列节点
     */
    private String monopolyQueueNode;
    /**
     * 连接状态
     */
    private ConnectionState connectionState;
    /**
     * 会话ID
     */
    private long sessionId;
    
    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    /**
     * Spring初始化方法
     */
    public void afterPropertiesSet() {
        this.connection();
        this.addListener();
    }

    /**
     * 连接
     */
    public void connection() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);
        zkTools = CuratorFrameworkFactory.builder().connectString(hosts).namespace(nameSpace).retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeOut == null ? 30000 : connectionTimeOut).sessionTimeoutMs(sessionTimeOut == null ? 30000 : sessionTimeOut).build();
        zkTools.start();
    }

    /**
     * 连接状态监听
     */
    public void addListener() {
        zkTools.getConnectionStateListenable().addListener((curatorFramework, connectionState) -> {
            log.info("state: " + connectionState.name());
            this.connectionState = connectionState;
            if (ConnectionState.CONNECTED == connectionState) {
                try {
                    sessionId = zkTools.getZookeeperClient().getZooKeeper().getSessionId();
                    registerMonopolyQueue();
                } catch (Exception e) {
                    log.error("注册独占队列失败");
                }
            }
            if (ConnectionState.RECONNECTED == connectionState) {
                try {
                    if (sessionId != zkTools.getZookeeperClient().getZooKeeper().getSessionId()) {
                        registerMonopolyQueue();
                    }
                } catch (Exception e) {
                    log.error("注册独占队列失败");
                }
            }
            if (connectionState.equals(ConnectionState.LOST)) {
            }
            if (connectionState.equals(ConnectionState.SUSPENDED)) {
            }
            if (connectionState.equals(ConnectionState.READ_ONLY)) {
            }
        });
    }

    /**
     * 注册独占队列
     */
    private void registerMonopolyQueue() throws Exception {
        if (zkTools.checkExists().watched().forPath(Constant.SEPARATOR + Constant.MONOPOLY) == null) {
            zkTools.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(Constant.SEPARATOR + Constant.MONOPOLY);
            log.info("创建独享锁队列节点成功！");
        }
        if (monopolyQueueNode == null || (monopolyQueueNode != null && zkTools.checkExists().forPath(monopolyQueueNode) == null)) {
            monopolyQueueNode = zkTools.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(Constant.SEPARATOR + Constant.MONOPOLY + Constant.SEPARATOR + Constant.QUEUE_NODE);
            log.info("成功加入独享锁队列");
        }
    }

    /**
     * 获得独占锁的执行权限
     * 
     * @return 执行权限标识
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean getMonopolyLock() throws Exception {
        boolean flag = false;
        if (connectionState != null && (connectionState.equals("CONNECTED") || connectionState.equals("RECONNECTED"))) {
            List<String> nodes = zkTools.getChildren().watched().forPath(Constant.SEPARATOR + Constant.MONOPOLY);
            if (nodes.size() > 0) {
                Collections.sort(nodes);
                // 判断当前应用是否在队列的第一位
                if ((Constant.SEPARATOR + Constant.MONOPOLY + Constant.SEPARATOR + nodes.get(0)).equals(monopolyQueueNode)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (zkTools != null) {
            zkTools.close();
            zkTools = null;
        }
    }
}
