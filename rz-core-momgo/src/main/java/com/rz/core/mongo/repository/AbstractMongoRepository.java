package com.rz.core.mongo.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rz.core.Assert;
import com.rz.core.mongo.source.PoDefinition;
import com.rz.core.mongo.source.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import com.rz.core.mongo.builder.MongoSort;
import net.sf.cglib.proxy.Enhancer;
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
    protected boolean autoCreateIndex;

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

    protected boolean getAutoCreateIndex() {
        return this.autoCreateIndex;
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

    public AbstractMongoRepository(
            Class<T> clazz,
            String rawConnectionString,
            String rawDatabaseName,
            String rawTableName,
            boolean autoCreateIndex) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotBlank(rawConnectionString, "rawConnectionString");
        Assert.isNotBlank(rawDatabaseName, "rawDatabaseName");
        Assert.isNotBlank(rawTableName, "rawTableName");

        this.poDefinition = SourcePool.getPoDefinition(clazz);
        this.rawConnectionString = rawConnectionString;
        this.rawDatabaseName = rawDatabaseName;
        this.rawTableName = rawTableName;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Executant.class);
        enhancer.setCallback(new ExecutantInterceptor());
        this.executant = (Executant<T>) enhancer.create(new Class[]{this.poDefinition.getClass()}, new Object[]{this.poDefinition});
        this.autoCreateIndex = autoCreateIndex;

        this.mongoClient = SourcePool.getMongoClient(this.rawConnectionString);
        this.mongoDatabase = SourcePool.getMongoDatabase(this.rawConnectionString, rawDatabaseName);
        this.mongoCollection = SourcePool.getMongoCollection(this.rawConnectionString, rawDatabaseName, rawTableName);

        if (this.autoCreateIndex) {
            List<PoFieldDefinition<T>> indexFieldDefinitions = this.poDefinition.getIndexFieldDefinitions();
            if (null != indexFieldDefinitions) {
                for (PoFieldDefinition indexFieldDefinition : indexFieldDefinitions) {
                    if (null == indexFieldDefinition) {
                        continue;
                    }

                    this.executant.createIndex(this.mongoCollection, indexFieldDefinition.getName(), indexFieldDefinition.isAscending());
                }
            }
            this.autoCreateIndex = false;
        }
    }

    @Override
    public PoDefinition<T> getPoDefinition() {
        return this.poDefinition;
    }

    @Override
    public void insert(T po) {
        this.executant.insert(this.getMongoCollection(), po);
    }

//    @Override
//    public void insert(List<T> pos) {
//        this.executant.insert(this.getMongoCollection(), pos);
//    }

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
    public T selectFirst(Bson filter, List<MongoSort> mongoSorts) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, mongoSorts);
    }

    @Override
    public Map selectById(Object id, String... fieldNames) {
        return this.executant.selectById(this.getMongoCollection(), id, fieldNames);
    }

    @Override
    public Map selectFirst(Bson filter, String... fieldNames) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, fieldNames);
    }

    @Override
    public Object max(String fieldName) {
        return this.executant.max(this.getMongoCollection(), fieldName);
    }

    @Override
    public Object min(String fieldName) {
        return this.executant.min(this.getMongoCollection(), fieldName);
    }

    @Override
    public Map selectFirst(Bson filter, List<MongoSort> mongoSorts, String... fieldNames) {
        return this.executant.selectFirst(this.getMongoCollection(), filter, mongoSorts, fieldNames);
    }

    @Override
    public List<T> selectByIds(List<Object> Ids) {
        return this.executant.selectByIds(this.getMongoCollection(), Ids);
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
    public List<Map> selectByIds(List<Object> ids, String... fieldNames) {
        return this.executant.selectByIds(this.getMongoCollection(), ids, fieldNames);
    }

    @Override
    public List<Map> select(Bson filter, String... fieldNames) {
        return this.executant.select(this.getMongoCollection(), filter, fieldNames);
    }

    @Override
    public List<Map> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames) {
        return this.executant.select(this.getMongoCollection(), filter, skip, limit, mongoSorts, fieldNames);
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
    public long updateById(Object id, Map<String, Object> values) {
        return this.executant.updateById(this.getMongoCollection(), id, values);
    }

    @Override
    public long update(Bson filter, Map<String, Object> values) {
        return this.executant.update(this.getMongoCollection(), filter, values);
    }

    @Override
    public long updateOrInsertById(Object id, T po) {
        return this.executant.updateOrInsertById(this.getMongoCollection(), id, po);
    }

    @Override
    public long updateById(Object id, T po) {
        return this.executant.updateById(this.getMongoCollection(), id, po);
    }

    @Override
    public long update(Bson filter, T po) {
        return this.executant.update(this.getMongoCollection(), filter, po);
    }

    @Override
    public Object increaseById(Object id, String fieldName, int number) {
        return this.executant.increaseById(this.getMongoCollection(), id, fieldName, number);
    }

    @Override
    public Object increase(String fieldName, Bson filter, int number) {
        return this.executant.increase(this.getMongoCollection(), filter, fieldName, number);
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
    public void createIndex(String fieldName, boolean isAscending) {
        this.executant.createIndex(this.getMongoCollection(), fieldName, isAscending);
    }

    @Override
    public DatabaseStatus getDatabaseStatus() {
        return this.executant.getDatabaseStatus(this.getMongoDatabase());
    }
}