package com.rz.core.zookeeper.event;

import com.rz.core.zookeeper.ClusterItem;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class MasterChangedEvent extends ZooKeeperEvent<ClusterItem> {
    public MasterChangedEvent(ClusterItem source) {
        super(source);
    }
}
