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
    private String connectionString;
    private String databaseName;
    private String tableName;

    private ShardingMongoRepositoryBuilder(Class<TPo> clazz, Shardingable<TSharding> shardingable) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotNull(shardingable, "shardingable");

        this.clazz = clazz;
        this.shardingable = shardingable;
        this.connectionString = "mongodb://localhost:27017/default";
    }

    public ShardingMongoRepositoryBuilder<TPo, TSharding> setConnectionString(String connectionString) {
        Assert.isNotBlank(connectionString, "connectionString");

        this.connectionString = connectionString;

        return this;
    }

    public ShardingMongoRepositoryBuilder<TPo, TSharding> setDatabaseName(String databaseName) {
        Assert.isNotBlank(databaseName, "databaseName");

        this.databaseName = databaseName;

        return this;
    }

    public ShardingMongoRepositoryBuilder setTableName(String tableName) {
        Assert.isNotBlank(tableName, "tableName");

        this.tableName = tableName;

        return this;
    }

    public ShardingMongoRepository<TPo, TSharding> build() {
        MongoClientURI mongoClientUri = new MongoClientURI(this.connectionString);
        if (StringUtils.isBlank(this.databaseName)) {
            this.databaseName = mongoClientUri.getDatabase();
        }
        if (StringUtils.isBlank(this.tableName)) {
            this.tableName = mongoClientUri.getCollection();
        }
        if (StringUtils.isBlank(this.tableName)) {
            this.tableName = this.clazz.getName();
        }

        return new DefaultShardingMongoRepository(this.clazz, this.shardingable, this.connectionString, this.databaseName, this.tableName);
    }

    public static <T, TSharding> ShardingMongoRepositoryBuilder<T, TSharding> create(Class<T> clazz, Shardingable<TSharding> shardingable) {
        return new ShardingMongoRepositoryBuilder<>(clazz, shardingable);
    }
}
