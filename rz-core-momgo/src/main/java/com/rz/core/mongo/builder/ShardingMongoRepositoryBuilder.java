package com.rz.core.mongo.builder;

import com.mongodb.MongoClientURI;
import com.rz.core.Assert;
import com.rz.core.mongo.repository.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class ShardingMongoRepositoryBuilder<TPo, TSharding> {
    private Class<TPo> clazz;
    private Shardingable<TSharding> shardingable;
    private boolean autoCreateIndex;

    private ShardingMongoRepositoryBuilder(Class<TPo> clazz, Shardingable<TSharding> shardingable) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotNull(shardingable, "shardingable");

        this.clazz = clazz;
        this.shardingable = shardingable;
    }

    public ShardingMongoRepositoryBuilder<TPo, TSharding> setAutoCreateIndex(boolean autoCreateIndex) {
        this.autoCreateIndex = autoCreateIndex;

        return this;
    }

    public ShardingMongoRepository<TPo, TSharding> build() {
        return new DefaultShardingMongoRepository<>(
                this.clazz,
                this.shardingable,
                this.shardingable.getRawConnectionString(),
                this.shardingable.getRawDatabaseName(),
                this.shardingable.getRawTableName(),
                this.autoCreateIndex);
    }

    public static <T, TSharding> ShardingMongoRepositoryBuilder<T, TSharding> create(Class<T> clazz, Shardingable<TSharding> shardingable) {
        return new ShardingMongoRepositoryBuilder<>(clazz, shardingable);
    }
}
