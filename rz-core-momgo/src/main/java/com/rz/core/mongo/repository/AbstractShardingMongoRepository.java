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
public abstract class AbstractShardingMongoRepository<TPo, TSharding>
        implements Shardingable<TSharding>, ShardingMongoRepository<TPo, TSharding> {
    protected PoDefinition<TPo> poDefinition;
    protected String rawConnectionString;
    protected String rawDatabaseName;
    protected String rawTableName;
    protected Executant<TPo> executant;
    protected Boolean autoCreateIndex;

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

        MongoCollection mongoCollection = SourcePool.getMongoCollection(connectionString, databaseName, tableName);

        if (this.autoCreateIndex) {
            synchronized (this.autoCreateIndex) {
                if (this.autoCreateIndex) {
                    List<PoFieldDefinition<TPo>> indexFieldDefinitions = this.poDefinition.getIndexFieldDefinitions();
                    if (null != indexFieldDefinitions) {
                        for (PoFieldDefinition indexFieldDefinition : indexFieldDefinitions) {
                            if (null == indexFieldDefinition) {
                                continue;
                            }

                            this.executant.createIndex(
                                    mongoCollection, indexFieldDefinition.getName(), indexFieldDefinition.isAscending());
                        }
                    }
                    this.autoCreateIndex = false;
                }
            }
        }

        return mongoCollection;
    }

    public AbstractShardingMongoRepository(
            Class<TPo> clazz,
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
        this.executant = (Executant<TPo>) enhancer.create(new Class[]{this.poDefinition.getClass()}, new Object[]{this.poDefinition});
        this.autoCreateIndex = autoCreateIndex;
    }

    @Override
    public PoDefinition<TPo> getPoDefinition() {
        return this.poDefinition;
    }

    @Override
    public String getRawConnectionString() {
        return this.rawConnectionString;
    }

    @Override
    public String getRawDatabaseName() {
        return this.rawDatabaseName;
    }

    @Override
    public String getRawTableName() {
        return this.rawTableName;
    }

    public Boolean getAutoCreateIndex(){
        return this.autoCreateIndex;
    }

    @Override
    public abstract String buildConnectionString(TSharding parameter);

    @Override
    public abstract String buildDatabaseName(TSharding parameter);

    @Override
    public abstract String buildTableName(TSharding parameter);

    @Override
    public void insert(TSharding parameter, TPo po) {
        this.executant.insert(this.getMongoCollection(parameter), po);
    }

//    @Override
//    public void insert(TSharding parameter, List<TPo> pos) {
//        this.executant.insert(this.getMongoCollection(parameter), pos);
//    }

    @Override
    public List<TPo> selectAll(TSharding parameter) {
        return this.executant.selectAll(this.getMongoCollection(parameter));
    }

    @Override
    public TPo selectById(TSharding parameter, Object id) {
        return this.executant.selectById(this.getMongoCollection(parameter), id);
    }

    @Override
    public TPo selectFirst(TSharding parameter, Bson filter) {
        return this.executant.selectFirst(this.getMongoCollection(parameter), filter);
    }

    @Override
    public Object max(TSharding parameter, String fieldName) {
        return this.executant.max(this.getMongoCollection(parameter), fieldName);
    }

    @Override
    public Object min(TSharding parameter, String fieldName) {
        return this.executant.min(this.getMongoCollection(parameter), fieldName);
    }

    @Override
    public TPo selectFirst(TSharding parameter, Bson filter, List<MongoSort> mongoSorts) {
        return this.executant.selectFirst(this.getMongoCollection(parameter), filter, mongoSorts);
    }

    @Override
    public Map selectById(TSharding parameter, Object id, String... fieldNames) {
        return this.executant.selectById(this.getMongoCollection(parameter), id, fieldNames);
    }

    @Override
    public Map selectFirst(TSharding parameter, Bson filter, String... fieldNames) {
        return this.executant.selectFirst(this.getMongoCollection(parameter), filter, fieldNames);
    }

    @Override
    public Map selectFirst(TSharding parameter, Bson filter, List<MongoSort> mongoSorts, String... fieldNames) {
        return this.executant.selectFirst(this.getMongoCollection(parameter), filter, mongoSorts, fieldNames);
    }

    @Override
    public List<TPo> selectByIds(TSharding parameter, List<Object> Ids) {
        return this.executant.selectByIds(this.getMongoCollection(parameter), Ids);
    }

    @Override
    public List<TPo> select(TSharding parameter, Bson filter) {
        return this.executant.select(this.getMongoCollection(parameter), filter);
    }

    @Override
    public List<TPo> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        return this.executant.select(this.getMongoCollection(parameter), filter, skip, limit, mongoSorts);
    }

    @Override
    public List<Map> selectByIds(TSharding parameter, List<Object> ids, String... fieldNames) {
        return this.executant.selectByIds(this.getMongoCollection(parameter), ids, fieldNames);
    }

    @Override
    public List<Map> select(TSharding parameter, Bson filter, String... fieldNames) {
        return this.executant.select(this.getMongoCollection(parameter), filter, fieldNames);
    }

    @Override
    public List<Map> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames) {
        return this.executant.select(this.getMongoCollection(parameter), filter, skip, limit, mongoSorts, fieldNames);
    }

    @Override
    public long deleteById(TSharding parameter, Object id) {
        return this.executant.deleteById(this.getMongoCollection(parameter), id);
    }

    @Override
    public long delete(TSharding parameter, Bson filter) {
        return this.executant.delete(this.getMongoCollection(parameter), filter);
    }

    @Override
    public long updateById(TSharding parameter, Object id, Map<String, Object> values) {
        return this.executant.updateById(this.getMongoCollection(parameter), id, values);
    }

    @Override
    public long update(TSharding parameter, Bson filter, Map<String, Object> values) {
        return this.executant.update(this.getMongoCollection(parameter), filter, values);
    }

    @Override
    public long updateOrInsertById(TSharding parameter, Object id, TPo po) {
        return this.executant.updateOrInsertById(this.getMongoCollection(parameter), id, po);
    }

    @Override
    public long updateById(TSharding parameter, Object id, TPo po) {
        return this.executant.updateById(this.getMongoCollection(parameter), id, po);
    }

    @Override
    public long update(TSharding parameter, Bson filter, TPo po) {
        return this.executant.update(this.getMongoCollection(parameter), filter, po);
    }

    @Override
    public Object increaseById(TSharding parameter, Object id, String fieldName, int number) {
        return this.executant.increaseById(this.getMongoCollection(parameter), id, fieldName, number);
    }

    @Override
    public Object increase(TSharding parameter, Bson filter, String fieldName, int number) {
        return this.executant.increase(this.getMongoCollection(parameter), filter, fieldName, number);
    }

    @Override
    public long count(TSharding parameter) {
        return this.executant.count(this.getMongoCollection(parameter));
    }

    @Override
    public long countById(TSharding parameter, Object id) {
        return this.executant.countById(this.getMongoCollection(parameter), id);
    }

    @Override
    public long count(TSharding parameter, Bson filter) {
        return this.executant.count(this.getMongoCollection(parameter), filter);
    }

    @Override
    public void createIndex(TSharding parameter, String fieldName, boolean isAscending) {
        this.executant.createIndex(this.getMongoCollection(parameter), fieldName, isAscending);
    }
}
