package com.rz.core.zookeeper;

import com.rz.core.zookeeper.listener.*;
import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public interface ClusterAgent {
    void listenItemsChanged(ItemsChangedListener listener);

    void listenMasterChanged(MasterChangedListener listener);

    void listenConnected(ZooKeeperListener listener);

    void listenDisconnected(ZooKeeperListener listener);

    boolean isConnected();

    ClusterItem getCurrent();

    ClusterItem getMaster();

    List<ClusterItem> getClusterItems();

    void acquireLock(String lockName) throws KeeperException, InterruptedException;

    void acquireLock(String lockName, int timeout) throws KeeperException, InterruptedException;

    void releaseLock(String lockName);

    ZooKeeperMaintainer getZooKeeperMaintainer();
}
