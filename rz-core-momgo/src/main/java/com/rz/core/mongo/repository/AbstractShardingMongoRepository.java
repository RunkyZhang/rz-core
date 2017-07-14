package com.rz.core.mongo.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneOptions;
import com.rz.core.Assert;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public abstract class AbstractShardingMongoRepository<TPo, TSharding> implements Shardingable<TSharding> {
    private PoDefinition poDefinition;
    protected String rawConnectionString;
    protected String rawDatabaseName;
    protected String rawTableName;

    protected String getRawConnectionString() {
        return this.rawConnectionString;
    }

    protected String getRawDatabaseName() {
        return this.rawDatabaseName;
    }

    protected String getRawTableName() {
        return this.rawTableName;
    }

    protected MongoClient getMongoClient(TSharding parameter) {
        String connectionString = this.buildConnectionString(parameter);

        return SourcePool.getMongoClient(connectionString);
    }

    protected MongoDatabase getMongoDatabase(TSharding parameter) {
        String connectionString = this.buildConnectionString(parameter);
        String databaseName = this.buildDatabaseName(parameter);

        return SourcePool.getMongoDatabase(connectionString, databaseName);
    }

    protected MongoCollection getMongoCollection(TSharding parameter) {
        String connectionString = this.buildConnectionString(parameter);
        String databaseName = this.buildDatabaseName(parameter);
        String tableName = this.buildTableName(parameter);

        return SourcePool.getMongoCollection(connectionString, databaseName, tableName);
    }

    public AbstractShardingMongoRepository(Class<TPo> poClass, String rawConnectionString, String rawDatabaseName, String rawTableName) {
        Assert.isNotNull(poClass, "poClass");
        Assert.isNotBlank(rawConnectionString, "rawConnectionString");
        Assert.isNotBlank(rawDatabaseName, "rawDatabaseName");
        Assert.isNotBlank(rawTableName, "rawTableName");

        this.poDefinition = SourcePool.getPoDefinition(poClass);
        this.rawConnectionString = rawConnectionString;
        this.rawDatabaseName = rawDatabaseName;
        this.rawTableName = rawTableName;
    }

    @Override
    public abstract String buildConnectionString(TSharding parameter);

    @Override
    public abstract String buildDatabaseName(TSharding parameter);

    @Override
    public abstract String buildTableName(TSharding parameter);

    public void insert(TSharding parameter, TPo po) {
        if (null == po) {
            return;
        }

        InsertOneOptions insertOneOptions = new InsertOneOptions();
        insertOneOptions.bypassDocumentValidation(false);

        this.getMongoCollection(parameter).insertOne(po, insertOneOptions);
    }
}
