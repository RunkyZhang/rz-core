package com.rz.core.mongo.repository;

import org.bson.conversions.Bson;

import java.util.Map;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class DefaultMongoRepository<T> extends AbstractMongoRepository<T> {
    public DefaultMongoRepository(
            Class<T> poClass,
            String rawConnectionString,
            String rawDatabaseName,
            String rawTableName,
            boolean autoCreateIndex) {
        super(poClass, rawConnectionString, rawDatabaseName, rawTableName, autoCreateIndex);
    }
}
