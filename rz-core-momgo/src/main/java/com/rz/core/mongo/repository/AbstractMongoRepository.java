package com.rz.core.mongo.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rz.core.Assert;
import com.rz.core.mongo.builder.MongoSort;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public abstract class AbstractMongoRepository<T> implements MongoRepository<T> {
    protected PoDefinition<T> poDefinition;
    protected String rawConnectionString;
    protected String rawDatabaseName;
    protected String rawTableName;
    protected Executant<T> executant;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection mongoCollection;

    protected String getRawConnectionString() {
        return this.rawConnectionString;
    }

    protected String getRawDatabaseName() {
        return this.rawDatabaseName;
    }

    protected String getRawTableName() {
        return this.rawTableName;
    }

    protected MongoClient getMongoClient() {
        return this.mongoClient;
    }

    protected MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    protected MongoCollection getMongoCollection() {
        return this.mongoCollection;
    }

    public AbstractMongoRepository(Class<T> clazz, String rawConnectionString, String rawDatabaseName, String rawTableName) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotBlank(rawConnectionString, "rawConnectionString");
        Assert.isNotBlank(rawDatabaseName, "rawDatabaseName");
        Assert.isNotBlank(rawTableName, "rawTableName");

        this.poDefinition = SourcePool.getPoDefinition(clazz);
        this.rawConnectionString = rawConnectionString;
        this.rawDatabaseName = rawDatabaseName;
        this.rawTableName = rawTableName;
        this.executant = new Executant(this.poDefinition);

        this.mongoClient = SourcePool.getMongoClient(this.rawConnectionString);
        this.mongoDatabase = SourcePool.getMongoDatabase(this.rawConnectionString, rawDatabaseName);
        this.mongoCollection = SourcePool.getMongoCollection(this.rawConnectionString, rawDatabaseName, rawTableName);
    }

    @Override
    public void insert(T po) {
        this.executant.insert(this.getMongoCollection(), po);
    }

    @Override
    public void insert(List<T> pos) {
        this.executant.insert(this.getMongoCollection(), pos);
    }

    @Override
    public List<T> selectAll() {
        return this.executant.selectAll(this.getMongoCollection());
    }

    @Override
    public T selectById(Object id) {
        return this.executant.selectById(this.getMongoCollection(), id);
    }

    @Override
    public T selectFirst(Bson filter) {
        return this.executant.selectFirst(this.getMongoCollection(), filter);
    }

    @Override
    public T selectFirst(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, skip, limit, mongoSorts);
    }

    @Override
    public Map selectById(Object id, String... feildNames) {
        return this.executant.selectById(this.getMongoCollection(), id, feildNames);
    }

    @Override
    public Map selectFirst(Bson filter, String... feildNames) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, feildNames);
    }

    @Override
    public Map selectFirst(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... feildNames) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, skip, limit, mongoSorts, feildNames);
    }

    @Override
    public List<T> select(Bson filter) {
        return this.executant.select(this.getMongoCollection(), filter);
    }

    @Override
    public List<T> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        return this.executant.select(this.getMongoCollection(), filter, skip, limit, mongoSorts);
    }

    @Override
    public List<Map> select(Bson filter, String... feildNames) {
        return this.executant.select(this.getMongoCollection(), filter, feildNames);
    }

    @Override
    public List<Map> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... feildNames) {
        return this.executant.select(this.getMongoCollection(), filter, skip, limit, mongoSorts, feildNames);
    }

    @Override
    public long count() {
        return this.executant.count(this.getMongoCollection());
    }

    @Override
    public long countById(Object id) {
        return this.executant.countById(this.getMongoCollection(), id);
    }

    @Override
    public long count(Bson filter) {
        return this.executant.count(this.getMongoCollection(), filter);
    }

    @Override
    public long deleteById(Object id) {
        return this.executant.deleteById(this.getMongoCollection(), id);
    }

    @Override
    public long delete(Bson filter) {
        return this.executant.delete(this.getMongoCollection(), filter);
    }

    @Override
    public long updateById(Object id, T po) {
        return this.executant.updateById(this.getMongoCollection(), id, po);
    }
}
