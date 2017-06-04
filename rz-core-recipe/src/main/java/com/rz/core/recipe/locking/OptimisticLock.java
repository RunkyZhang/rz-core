package com.rz.core.recipe.locking;

import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticLock {
    private AtomicInteger version;

    public OptimisticLock() {
        this.version = new AtomicInteger(0);
    }

    public boolean acquire(int currentVersion) {
        int next = currentVersion + 1;

        return this.version.compareAndSet(currentVersion, next);
    }

    public int getVersion() {
        return this.version.intValue();
    }
}
