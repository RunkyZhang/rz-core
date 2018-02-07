package com.rz.core.async;

import java.io.Closeable;

/**
 * Created by renjie.zhang on 2/6/2018.
 */
public interface AsyncJobTrigger extends Closeable {
    boolean isDisposed();

    void add(AsyncJob asyncJob);

    AsyncJobTrigger start();
}
