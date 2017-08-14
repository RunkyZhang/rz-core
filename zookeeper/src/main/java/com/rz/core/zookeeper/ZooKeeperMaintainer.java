package com.rz.core.zookeeper;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import com.rz.core.Assert;
import com.rz.core.Tuple2;
import com.rz.core.RZHelper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
class ZooKeeperMaintainer implements Watcher, Closeable {
    private boolean isDisposed;
    private boolean isInitialized;
    private String connectString;
    private int sessionTimeout;
    private ZooKeeper zooKeeper;
    private Consumer<ZooKeeper> initializeCallback;
    private Consumer<Tuple2<WatchedEvent, ZooKeeper>> _processCallback;
    private ReadWriteLock readWriteLock;

    public ZooKeeper getZooKeeper() {
        try {
            this.readWriteLock.readLock().lock();

            return this.zooKeeper;
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public ZooKeeperMaintainer(
            String connectString,
            int sessionTimeout,
            Consumer<Tuple2<WatchedEvent, ZooKeeper>> processCallback,
            Consumer<ZooKeeper> initializeCallback) throws IOException {
        Assert.isNotBlank(connectString, "connectString");

        this.isDisposed = false;
        this.readWriteLock = new ReentrantReadWriteLock();
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
        this.initializeCallback = initializeCallback;
        this._processCallback = processCallback;

        this.isInitialized = true;// for block event SyncConnected in method Process at the same time
        this.zooKeeper = new ZooKeeper(this.connectString, this.sessionTimeout, this);
        if (this.waitConnectDone()) {
            this.isInitialized = this.runInitialization();
        } else {
            this.isInitialized = false;
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Assert.isNotNull(watchedEvent, "watchedEvent");

        boolean hasException = false;
        if (Event.EventType.None == watchedEvent.getType() && Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            try {
                this.readWriteLock.writeLock().lock();

                if (this.waitConnectDone() && !this.isInitialized) {
                    this.isInitialized = this.runInitialization();
                }
            } catch (Throwable throwable) {
                hasException = true;

                throwable.printStackTrace();
                // log
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        } else if (Event.EventType.None == watchedEvent.getType() && Event.KeeperState.Expired == watchedEvent.getState()) {
            try {
                this.readWriteLock.writeLock().lock();

                try {
                    this.zooKeeper.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                this.isInitialized = true;// for block event SyncConnected in method Process at the same time
                this.zooKeeper = new ZooKeeper(this.connectString, this.sessionTimeout, this);
                if (this.waitConnectDone()) {
                    this.isInitialized = this.runInitialization();
                } else {
                    this.isInitialized = false;
                }
            } catch (Throwable throwable) {
                hasException = true;

                throwable.printStackTrace();
                // log
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        if (null != this._processCallback && !hasException) {
            try {
                this._processCallback.accept(new Tuple2<>(watchedEvent, this.zooKeeper));
            } catch (Throwable throwable) {
                throwable.printStackTrace();

                // log
                System.out.println("Failed to invoke [_processCallback].");
            }
        }
    }

    private boolean waitConnectDone() {
        boolean flag = false;
        for (int i = 0; i < 256; i++) {
            if (ZooKeeper.States.CONNECTED == this.zooKeeper.getState()) {
                flag = true;
                break;
            }

            ZGHelper.saftSleep(1);
        }

        return flag;
    }

    private boolean runInitialization() {
        if (null == this.initializeCallback) {
            return true;
        } else {
            try {
                this.initializeCallback.accept(this.zooKeeper);

                return true;
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof KeeperException.ConnectionLossException
                        || throwable.getCause() instanceof KeeperException.SessionExpiredException
                        || throwable.getCause() instanceof TimeoutException) {
                    return false;
                } else {
                    throw throwable;
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!this.isDisposed) {
            try {
                this.readWriteLock.writeLock().lock();

                this.zooKeeper.close();
            } catch (Throwable throwable) {
                // log
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        this.isDisposed = true;
    }
}
