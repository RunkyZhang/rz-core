package com.rz.core.async;

import java.io.Closeable;

/**
 * Created by renjie.zhang on 1/11/2018.
 */
public interface AsyncJobWorker extends Closeable {
    boolean isDisposed();

    AsyncJobWorker start();

    boolean add(AsyncJob job);
}
