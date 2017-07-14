package com.rz.core.mongo.repository;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public interface Shardingable<T> {
    String buildConnectionString(T parameter);

    String buildDatabaseName(T parameter);

    String buildTableName(T parameter);
}
