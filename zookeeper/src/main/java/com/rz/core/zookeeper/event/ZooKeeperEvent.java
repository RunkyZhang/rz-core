package com.rz.core.zookeeper.event;

import java.util.EventObject;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class ZooKeeperEvent<T> extends EventObject {
    public static ZooKeeperEvent<Object> Empty = new ZooKeeperEvent<>(null);

    @Override
    public T getSource() {
        return (T) source;
    }

    public ZooKeeperEvent(T source) {
        super(source);
    }
}
