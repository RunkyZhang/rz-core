package com.rz.core.mongo.builder;

import com.mongodb.MongoClientURI;
import com.rz.core.Assert;
import com.rz.core.mongo.repository.DefaultMongoRepository;
import com.rz.core.mongo.repository.MongoRepository;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class MongoRepositoryBuilder<T> {
    private Class<T> clazz;
    private String connectionString;
    private String databaseName;
    private String tableName;

    private MongoRepositoryBuilder(Class<T> clazz) {
        Assert.isNotNull(clazz, "clazz");

        this.clazz = clazz;
        this.connectionString = "mongodb://localhost:27017/default";
    }

    public MongoRepositoryBuilder<T> setConnectionString(String connectionString) {
        Assert.isNotBlank(connectionString, "connectionString");

        this.connectionString = connectionString;

        return this;
    }

    public MongoRepositoryBuilder<T> setDatabaseName(String databaseName) {
        Assert.isNotBlank(databaseName, "databaseName");

        this.databaseName = databaseName;

        return this;
    }

    public MongoRepositoryBuilder setTableName(String tableName) {
        Assert.isNotBlank(tableName, "tableName");

        this.tableName = tableName;

        return this;
    }

    public MongoRepository<T> build() {
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

        return new DefaultMongoRepository<T>(this.clazz, this.connectionString, this.databaseName, this.tableName);
    }

    public static <T> MongoRepositoryBuilder<T> create(Class<T> clazz) {
        return new MongoRepositoryBuilder<T>(clazz);
    }
}
