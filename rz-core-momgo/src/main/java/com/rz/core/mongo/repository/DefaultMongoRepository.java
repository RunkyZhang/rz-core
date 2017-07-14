package com.rz.core.mongo.repository;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class DefaultMongoRepository<T> extends AbstractMongoRepository<T> {
    public DefaultMongoRepository(Class<T> poClass, String rawConnectionString, String rawDatabaseName, String rawTableName) {
        super(poClass, rawConnectionString, rawDatabaseName, rawTableName);
    }
}
