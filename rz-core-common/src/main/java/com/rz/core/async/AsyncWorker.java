package com.rz.core.async;

import java.io.Closeable;

/**
 * Created by renjie.zhang on 1/11/2018.
 */
public interface AsyncWorker extends Closeable {
    boolean isDisposed();

    AsyncWorker start();

    boolean add(AsyncJob job);
}
