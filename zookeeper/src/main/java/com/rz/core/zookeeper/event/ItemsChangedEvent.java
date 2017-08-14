package com.rz.core.zookeeper.event;

import com.rz.core.zookeeper.ClusterItem;

import java.util.List;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class ItemsChangedEvent extends ZooKeeperEvent<List<ClusterItem>> {
    public ItemsChangedEvent(List<ClusterItem> source) {
        super(source);
    }
}
