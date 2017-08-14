package com.rz.core.zookeeper.listener;

import com.rz.core.zookeeper.event.ZooKeeperEvent;

import java.util.EventListener;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public interface ZooKeeperListener<T extends ZooKeeperEvent> extends EventListener {
    void onZooKeeperEvent(Object sender, T e);
}
