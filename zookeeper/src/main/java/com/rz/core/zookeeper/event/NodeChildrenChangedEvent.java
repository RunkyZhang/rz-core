package com.rz.core.zookeeper.event;

import java.util.List;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class NodeChildrenChangedEvent extends ZooKeeperEvent<List<String>> {
    public NodeChildrenChangedEvent(List<String> source) {
        super(source);
    }
}
