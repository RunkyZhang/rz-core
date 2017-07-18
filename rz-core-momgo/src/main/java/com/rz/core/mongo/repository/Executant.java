package com.rz.core.mongo.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.rz.core.Assert;
import com.rz.core.RZHelper;
import com.rz.core.mongo.builder.MongoSort;
import com.rz.core.mongo.builder.MongoSortBuilder;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
class Executant<T> {
    private PoDefinition<T> poDefinition;

    Executant(PoDefinition<T> poDefinition) {
        this.poDefinition = poDefinition;
    }

    void insert(MongoCollection mongoCollection, T po) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return;
        }

        InsertOneOptions insertOneOptions = new InsertOneOptions();
        insertOneOptions.bypassDocumentValidation(false);

        mongoCollection.insertOne(BsonMapper.toDocument(po, false), insertOneOptions);
    }

    void insert(MongoCollection mongoCollection, List<T> pos) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == pos) {
            return;
        }

        InsertManyOptions insertManyOptions = new InsertManyOptions();
        insertManyOptions.bypassDocumentValidation(false);

        mongoCollection.insertMany(BsonMapper.toDocument(pos, false), insertManyOptions);
    }

    List<T> selectAll(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = mongoCollection.find();
        if (null == findIterable) {
            return null;
        }

        return BsonMapper.toObject(findIterable.iterator(), poDefinition.getPoClazz());
    }

    T selectById(MongoCollection mongoCollection, Object id) {
        return this.selectFirst(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
    }

    T selectFirst(MongoCollection mongoCollection, Bson filter) {
        return this.selectFirst(mongoCollection, filter, (List<MongoSort>) null);
    }

    T selectFirst(MongoCollection mongoCollection, Bson filter, List<MongoSort> mongoSorts) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, 0, null, mongoSorts);

        if (null == findIterable) {
            return null;
        }
        Document document = (Document) findIterable.first();

        return BsonMapper.toObject(document, this.poDefinition.getPoClazz());
    }

    Map selectById(MongoCollection mongoCollection, Object id, String... fieldNames) {
        return this.selectFirst(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id), fieldNames);
    }

    Map selectFirst(MongoCollection mongoCollection, Bson filter, String... fieldNames) {
        return this.selectFirst(mongoCollection, filter, null, fieldNames);
    }

    Object max(MongoCollection mongoCollection, String fieldName) {
        Map map = this.selectFirst(mongoCollection, null, Arrays.asList(new MongoSort(fieldName, false)), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    Object min(MongoCollection mongoCollection, String fieldName) {
        Map map = this.selectFirst(mongoCollection, null, Arrays.asList(new MongoSort(fieldName, true)), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    Map selectFirst(MongoCollection mongoCollection, Bson filter, List<MongoSort> mongoSorts, String... fieldNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, 0, null, mongoSorts, fieldNames);

        if (null == findIterable) {
            return null;
        }
        Document document = (Document) findIterable.first();

        return BsonMapper.toMap(document, this.poDefinition.getPoClazz(), fieldNames);
    }

    List<T> select(MongoCollection mongoCollection, Bson filter) {
        return this.select(mongoCollection, filter, 0, null, null);
    }

    List<T> select(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts);

        if (null == findIterable) {
            return null;
        }

        return BsonMapper.toObject(findIterable.iterator(), this.poDefinition.getPoClazz());
    }

    List<Map> select(MongoCollection mongoCollection, Bson filter, String... feildNames) {
        return this.select(mongoCollection, filter, 0, null, null, feildNames);
    }

    List<Map> select(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        FindIterable findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, fieldNames);

        if (null == findIterable) {
            return null;
        }

        return BsonMapper.toMap(findIterable.iterator(), this.poDefinition.getPoClazz(), fieldNames);
    }

    long deleteById(MongoCollection mongoCollection, Object id) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        DeleteOptions deleteOptions = new DeleteOptions();
        DeleteResult deleteResult = mongoCollection.deleteOne(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id), deleteOptions);
        if (null == deleteResult) {
            return 0;
        }

        return deleteResult.getDeletedCount();
    }

    long delete(MongoCollection mongoCollection, Bson filter) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        DeleteOptions deleteOptions = new DeleteOptions();
        DeleteResult deleteResult = mongoCollection.deleteMany(this.formatFilter(filter), deleteOptions);
        if (null == deleteResult) {
            return 0;
        }

        return deleteResult.getDeletedCount();
    }

    long updateById(MongoCollection mongoCollection, Object id, Map<String, Object> values) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (RZHelper.isEmptyCollection(values)) {
            return 0;
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(false);
        UpdateResult updateResult = mongoCollection.updateOne(
                this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)),
                new Document("$set", BsonMapper.toDocument(values, this.poDefinition.getPoClazz())),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    long update(MongoCollection mongoCollection, Bson filter, Map<String, Object> values) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (RZHelper.isEmptyCollection(values)) {
            return 0;
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        UpdateResult updateResult = mongoCollection.updateMany(
                this.formatFilter(filter),
                new Document("$set", BsonMapper.toDocument(values, this.poDefinition.getPoClazz())),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    long updateById(MongoCollection mongoCollection, Object id, T po) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return 0;
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(false);
        UpdateResult updateResult = mongoCollection.updateOne(
                this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)),
                new Document("$set", BsonMapper.toDocument(po, true)),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    long update(MongoCollection mongoCollection, Bson filter, T po) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return 0;
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(false);
        UpdateResult updateResult = mongoCollection.updateMany(
                this.formatFilter(filter),
                new Document("$set", BsonMapper.toDocument(po, true)),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    Object increaseById(MongoCollection mongoCollection, Object id, String fieldName, int number) {
        return this.increase(mongoCollection, this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)), fieldName, number);
    }

    Object increase(MongoCollection mongoCollection, Bson filter, String fieldName, int number) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotBlank(fieldName, "fieldName");

        FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();
        findOneAndUpdateOptions.upsert(false);
        findOneAndUpdateOptions.bypassDocumentValidation(false);
        Document document = (Document) mongoCollection.findOneAndUpdate(
                this.formatFilter(filter), Updates.inc(fieldName, number), findOneAndUpdateOptions);
        Map map = BsonMapper.toMap(document, this.poDefinition.getPoClazz(), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    long count(MongoCollection mongoCollection, Bson filter) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        CountOptions countOptions = new CountOptions();
        return mongoCollection.count(this.formatFilter(filter), countOptions);
    }

    long count(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        return mongoCollection.count();
    }

    long countById(MongoCollection mongoCollection, Object id) {
        return this.count(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
    }

    private FindIterable buildFindIterable(
            MongoCollection mongoCollection,
            Bson filter,
            int skip,
            Integer limit,
            List<MongoSort> mongoSorts,
            String... fieldNames) {
        FindIterable findIterable = mongoCollection.find(this.formatFilter(filter));

        if (0 < skip) {
            findIterable.skip(skip);
        }

        if (null != limit) {
            findIterable.limit(limit < 0 ? 0 : limit);
        }

        if (!RZHelper.isEmptyCollection(mongoSorts)) {
            mongoSorts = BsonMapper.formatId(mongoSorts, this.poDefinition.getPoClazz());
            MongoSortBuilder sortByBuilder = null;
            for (MongoSort mongoSort : mongoSorts) {
                if (null != mongoSort) {
                    sortByBuilder = null == sortByBuilder ? MongoSortBuilder.createByAppand(mongoSort) : sortByBuilder.appand(mongoSort);
                }
            }
            findIterable.sort(Sorts.orderBy(sortByBuilder.build()));
        }

        if (null != fieldNames && 0 < fieldNames.length) {
            findIterable.projection(Projections.include(BsonMapper.formatId(fieldNames, this.poDefinition.getPoClazz())));
        }

        return findIterable;
    }

    private Bson formatFilter(Bson filter) {
        if (null == filter) {
            return new Document();
        } else {
            return filter;
        }
    }
}
