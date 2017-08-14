package com.rz.core.zookeeper;

import java.util.concurrent.Semaphore;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
class ClusterLock {
    private long createdTime;
    private Semaphore semaphore;

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
}
