package com.rz.core.function;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
@FunctionalInterface
public interface FunctionEx<T, R> {
    R apply(T t) throws Throwable;
}
