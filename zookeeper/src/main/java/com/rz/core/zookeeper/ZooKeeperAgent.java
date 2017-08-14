package com.rz.core.zookeeper;

import com.rz.core.zookeeper.listener.*;
import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public interface ZooKeeperAgent {
    void listenNodeValueChanged(String path, NodeValueChangedListener listener);

    void listenNodeChildrenChanged(String path, NodeChildrenChangedListener listener);

    void listenItemsChanged(ItemsChangedListener listener);

    void listenMasterChanged(MasterChangedListener listener);

    void listenConnected(ZooKeeperListener listener);

    void listenDisconnected(ZooKeeperListener listener);

    boolean isConnected();

    ClusterItem getCurrent();

    ClusterItem getMaster();

    List<ClusterItem> getClusterItems();

    void acquireLock(String lockName);

    void acquireLock(String lockName, int timeout) throws KeeperException, InterruptedException;

    void releaseLock(String lockName);

    void Start();

    void Stop();
}
