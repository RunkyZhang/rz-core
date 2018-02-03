package com.rz.core.function;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
@FunctionalInterface
public interface SupplierEx<T> {
    T get() throws Throwable;
}
