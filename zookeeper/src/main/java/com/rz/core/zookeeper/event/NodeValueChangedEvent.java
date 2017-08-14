package com.rz.core.zookeeper.event;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class NodeValueChangedEvent extends ZooKeeperEvent<String> {
    public NodeValueChangedEvent(String source) {
        super(source);
    }
}
