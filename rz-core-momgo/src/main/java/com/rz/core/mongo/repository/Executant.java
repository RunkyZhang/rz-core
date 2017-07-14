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

import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
class Executant<T> {
    private PoDefinition<T> poDefinition;

    public Executant(PoDefinition<T> poDefinition) {
        this.poDefinition = poDefinition;
    }

    public void insert(MongoCollection mongoCollection, T po) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return;
        }

        InsertOneOptions insertOneOptions = new InsertOneOptions();
        insertOneOptions.bypassDocumentValidation(false);

        mongoCollection.insertOne(BsonMapper.toDocument(po), insertOneOptions);
    }

    public void insert(MongoCollection mongoCollection, List<T> pos) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == pos) {
            return;
        }

        InsertManyOptions insertManyOptions = new InsertManyOptions();
        insertManyOptions.bypassDocumentValidation(false);

        mongoCollection.insertMany(BsonMapper.toDocument(pos), insertManyOptions);
    }

    public List<T> selectAll(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable<Document> findIterable = mongoCollection.find();
        if (null == findIterable || null == findIterable.iterator()) {
            return null;
        }

        return BsonMapper.toObject(findIterable.iterator(), poDefinition.getClazz());
    }

    public T selectFirst(MongoCollection mongoCollection, Bson filter) {
        return this.selectFirst(mongoCollection, filter, 0, null, null);
    }

    public T selectFirst(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable<Document> findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, new String[]{});

        Document document = null;
        if (null == findIterable || null == (document = findIterable.first())) {
            return null;
        }

        return BsonMapper.toObject(document, this.poDefinition.getClazz());
    }

    public Map selectFirst(MongoCollection mongoCollection, Bson filter, String... feildNames) {
        return this.selectFirst(mongoCollection, filter, 0, null, null, feildNames);
    }

    public Map selectFirst(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... feildNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(feildNames, "feildNames");

        FindIterable<Document> findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, feildNames);

        Document document = null;
        if (null == findIterable || null == (document = findIterable.first())) {
            return null;
        }

        return BsonMapper.toMap(document, this.poDefinition.getClazz());
    }

    public T selectById(MongoCollection mongoCollection, Object id) {
        return this.selectFirst(mongoCollection, Filters.eq(PoDefinition.MONGO_ID_FIELD_NAME, id), 0, null, null);
    }

    public List<T> select(MongoCollection mongoCollection, Bson filter) {
        return this.select(mongoCollection, filter, 0, null, null);
    }

    public List<T> select(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable<Document> findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, new String[]{});

        if (null == findIterable || null == findIterable.iterator()) {
            return null;
        }

        return BsonMapper.toObject(findIterable.iterator(), this.poDefinition.getClazz());
    }

    public Map selectById(MongoCollection mongoCollection, Object id, String... feildNames) {
        return this.selectFirst(mongoCollection, Filters.eq(PoDefinition.MONGO_ID_FIELD_NAME, id), 0, null, null, feildNames);
    }

    public List<Map> select(MongoCollection mongoCollection, Bson filter, String... feildNames) {
        return this.select(mongoCollection, filter, 0, null, null, feildNames);
    }

    public List<Map> select(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... feildNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(feildNames, "feildNames");

        FindIterable<Document> findIterable = buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, feildNames);

        if (null == findIterable || null == findIterable.iterator()) {
            return null;
        }

        return BsonMapper.toMap(findIterable.iterator(), this.poDefinition.getClazz());
    }

    public long count(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        return mongoCollection.count();
    }

    public long countById(MongoCollection mongoCollection, Object id) {
        return this.count(mongoCollection, Filters.eq(PoDefinition.MONGO_ID_FIELD_NAME, id));
    }

    public long count(MongoCollection mongoCollection, Bson filter) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        CountOptions countOptions = new CountOptions();
        return mongoCollection.count(this.formatFilter(filter), countOptions);
    }


    public long deleteById(MongoCollection mongoCollection, Object id) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        DeleteOptions deleteOptions = new DeleteOptions();
        DeleteResult deleteResult = mongoCollection.deleteOne(Filters.eq(PoDefinition.MONGO_ID_FIELD_NAME, id), deleteOptions);
        if (null == deleteResult) {
            return 0;
        }

        return deleteResult.getDeletedCount();
    }

    public long delete(MongoCollection mongoCollection, Bson filter) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        DeleteOptions deleteOptions = new DeleteOptions();
        DeleteResult deleteResult = mongoCollection.deleteMany(this.formatFilter(filter), deleteOptions);
        if (null == deleteResult) {
            return 0;
        }

        return deleteResult.getDeletedCount();
    }

    public long updateById(MongoCollection mongoCollection, Object id, T po) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == id) {
            return 0;
        }

        UpdateOptions updateOptions = new UpdateOptions();
        UpdateResult updateResult = mongoCollection.updateOne(
                Filters.eq(PoDefinition.MONGO_ID_FIELD_NAME, id), new Document("$set", BsonMapper.toDocument(po)), updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    private FindIterable<Document> buildFindIterable(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... feildNames) {
        FindIterable<Document> findIterable = mongoCollection.find(this.formatFilter(filter));

        if (0 < skip) {
            findIterable.skip(skip);
        }

        if (null != limit) {
            findIterable.limit(limit.intValue() < 0 ? 0 : limit.intValue());
        }

        if (!RZHelper.isEmptyCollection(mongoSorts)) {
            mongoSorts = BsonMapper.formatId(mongoSorts, this.poDefinition.getClazz());
            MongoSortBuilder sortByBuilder = null;
            for (MongoSort mongoSort : mongoSorts) {
                if (null != mongoSort) {
                    sortByBuilder = null == sortByBuilder ? MongoSortBuilder.createByAppand(mongoSort) : sortByBuilder.appand(mongoSort);
                }
            }
            findIterable.sort(Sorts.orderBy(sortByBuilder.build()));
        }

        if (null != feildNames && 0 < feildNames.length) {
            findIterable.projection(Projections.include(BsonMapper.formatId(feildNames, this.poDefinition.getClazz())));
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
