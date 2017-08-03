package com.rz.core.mongo.repository;

import com.rz.core.Assert;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class DefaultShardingMongoRepository<TPo, TSharding> extends AbstractShardingMongoRepository<TPo, TSharding> {
    private Shardingable<TSharding> shardingable;

    public DefaultShardingMongoRepository(
            Class<TPo> poClass,
            Shardingable<TSharding> shardingable,
            String rawConnectionString,
            String rawDatabaseName,
            String rawTableName,
            boolean autoCreateIndex) {
        super(poClass, rawConnectionString, rawDatabaseName, rawTableName, autoCreateIndex);

        Assert.isNotNull(shardingable, "shardingable");

        this.shardingable = shardingable;
    }

    @Override
    public String buildConnectionString(TSharding parameter) {
        return this.shardingable.buildConnectionString(parameter);
    }

    @Override
    public String buildDatabaseName(TSharding parameter) {
        return this.shardingable.buildDatabaseName(parameter);
    }

    @Override
    public String buildTableName(TSharding parameter) {
        return this.shardingable.buildTableName(parameter);
    }
}
