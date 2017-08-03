package com.rz.core.mongo.repository;

import com.mongodb.Function;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.rz.core.Assert;
import com.rz.core.RZHelper;
import com.rz.core.mongo.builder.MongoSort;
import com.rz.core.mongo.builder.MongoSortBuilder;
import com.rz.core.mongo.mapper.InsertDataMapper;
import com.rz.core.mongo.mapper.SelectDataMapper;
import com.rz.core.mongo.mapper.UpdateDataMapper;
import com.rz.core.mongo.source.PoDefinition;
import com.rz.core.mongo.source.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
public class Executant<T> {
    private PoDefinition<T> poDefinition;

    public Executant(PoDefinition<T> poDefinition) {
        this.poDefinition = poDefinition;
    }

    public void insert(MongoCollection mongoCollection, T po) {
        this.insert(mongoCollection, po, null);
    }

    public void insert(MongoCollection mongoCollection, T po, Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return;
        }

        PoFieldDefinition<T> idFieldDefinition = this.poDefinition.getIdFieldDefinition();

        // if exist id and id is objectId type(auto increase)
        Object id = null;
        if (null != idFieldDefinition) {
            try {
                id = idFieldDefinition.getValue(po);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();

                throw new MongoException(String.format("Failed to get id field(%s) value.", idFieldDefinition.getName()), e);
            }

            if (!idFieldDefinition.isObjectId()) {
                Assert.isNotNull(id, "po." + idFieldDefinition.getName());
            }
        }

        InsertOneOptions insertOneOptions = new InsertOneOptions();
        insertOneOptions.bypassDocumentValidation(false);

        Document document = InsertDataMapper.toDocument(po);
        if (null != function) {
            document = function.apply(document);
        }
        mongoCollection.insertOne(document, insertOneOptions);

        // re-back id to po
        if (null != idFieldDefinition
                && idFieldDefinition.isObjectId()
                && document.containsKey(PoFieldDefinition.MONGO_ID_FIELD_NAME)) {
            try {
                idFieldDefinition.setValue(po, document.get(PoFieldDefinition.MONGO_ID_FIELD_NAME));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

//    void insert(MongoCollection mongoCollection, List<T> pos) {
//        Assert.isNotNull(mongoCollection, "mongoCollection");
//        if (RZHelper.isEmptyCollection(pos)) {
//            return;
//        }
//
//        InsertManyOptions insertManyOptions = new InsertManyOptions();
//        insertManyOptions.bypassDocumentValidation(false);
//
//        mongoCollection.insertMany(BsonMapper.toDocument(pos, false), insertManyOptions);
//    }

    public List<T> selectAll(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = mongoCollection.find();
        if (null == findIterable) {
            return null;
        }

        return SelectDataMapper.toObject(findIterable.iterator(), poDefinition.getPoClazz());
    }

    public T selectById(MongoCollection mongoCollection, Object id) {
        if (null == id) {
            return null;
        }

        return this.selectFirst(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
    }

    public T selectFirst(MongoCollection mongoCollection, Bson filter) {
        return this.selectFirst(mongoCollection, filter, (List<MongoSort>) null);
    }

    public T selectFirst(MongoCollection mongoCollection, Bson filter, List<MongoSort> mongoSorts) {
        return this.selectFirst(mongoCollection, filter, mongoSorts, (Function<Document, Document>) null);
    }

    public T selectFirst(
            MongoCollection mongoCollection,
            Bson filter,
            List<MongoSort> mongoSorts,
            Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, 0, null, mongoSorts);

        if (null == findIterable) {
            return null;
        }
        Document document = (Document) findIterable.first();
        if (null != function) {
            document = function.apply(document);
        }

        return SelectDataMapper.toObject(document, this.poDefinition.getPoClazz());
    }

    public Map selectById(MongoCollection mongoCollection, Object id, String... fieldNames) {
        if (null == id) {
            return null;
        }

        return this.selectFirst(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id), fieldNames);
    }

    public Map selectFirst(MongoCollection mongoCollection, Bson filter, String... fieldNames) {
        return this.selectFirst(mongoCollection, filter, null, fieldNames);
    }

    public Object max(MongoCollection mongoCollection, String fieldName) {
        Map map = this.selectFirst(mongoCollection, null, Arrays.asList(new MongoSort(fieldName, false)), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    public Object min(MongoCollection mongoCollection, String fieldName) {
        Map map = this.selectFirst(mongoCollection, null, Arrays.asList(new MongoSort(fieldName, true)), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    public Map selectFirst(MongoCollection mongoCollection, Bson filter, List<MongoSort> mongoSorts, String... fieldNames) {
        return this.selectFirst(mongoCollection, filter, mongoSorts, null, fieldNames);
    }

    public Map selectFirst(
            MongoCollection mongoCollection,
            Bson filter, List<MongoSort> mongoSorts,
            Function<Document, Document> function,
            String... fieldNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, 0, null, mongoSorts, fieldNames);

        if (null == findIterable) {
            return null;
        }
        Document document = (Document) findIterable.first();
        if (null != function) {
            document = function.apply(document);
        }

        return SelectDataMapper.toMap(document, this.poDefinition.getPoClazz(), fieldNames);
    }

    public List<T> selectByIds(MongoCollection mongoCollection, List<Object> ids) {
        if (null == ids) {
            return null;
        }
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        List<Bson> filters = new ArrayList<>();
        for (Object id : ids) {
            if (null != id) {
                filters.add(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
            }
        }
        if (filters.isEmpty()) {
            return new ArrayList<>();
        }

        return this.select(mongoCollection, Filters.or(filters));
    }

    public List<T> select(MongoCollection mongoCollection, Bson filter) {
        return this.select(mongoCollection, filter, 0, null, null);
    }

    public List<T> select(MongoCollection mongoCollection, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts) {
        return this.select(mongoCollection, filter, skip, limit, mongoSorts, (Function<Iterator<Document>, Iterator<Document>>) null);
    }

    public List<T> select(
            MongoCollection mongoCollection,
            Bson filter,
            int skip,
            Integer limit,
            List<MongoSort> mongoSorts,
            Function<Iterator<Document>, Iterator<Document>> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts);

        if (null == findIterable) {
            return null;
        }

        Iterator<Document> documents = findIterable.iterator();
        if (null != function) {
            documents = function.apply(documents);
        }

        return SelectDataMapper.toObject(documents, this.poDefinition.getPoClazz());
    }

    public List<Map> selectByIds(MongoCollection mongoCollection, List<Object> ids, String... fieldNames) {
        if (null == ids) {
            return null;
        }
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        List<Bson> filters = new ArrayList<>();
        for (Object id : ids) {
            if (null != id) {
                filters.add(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
            }
        }
        if (filters.isEmpty()) {
            return new ArrayList<>();
        }

        return this.select(mongoCollection, Filters.or(filters), fieldNames);
    }

    public List<Map> select(MongoCollection mongoCollection, Bson filter, String... fieldNames) {
        return this.select(mongoCollection, filter, 0, null, null, fieldNames);
    }

    public List<Map> select(
            MongoCollection mongoCollection,
            Bson filter,
            int skip,
            Integer limit,
            List<MongoSort> mongoSorts,
            String... fieldNames) {
        return select(mongoCollection, filter, skip, limit, mongoSorts, null, fieldNames);
    }

    public List<Map> select(
            MongoCollection mongoCollection,
            Bson filter,
            int skip,
            Integer limit,
            List<MongoSort> mongoSorts,
            Function<Iterator<Document>, Iterator<Document>> function,
            String... fieldNames) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        FindIterable findIterable = this.buildFindIterable(mongoCollection, filter, skip, limit, mongoSorts, fieldNames);

        if (null == findIterable) {
            return null;
        }

        Iterator<Document> documents = findIterable.iterator();
        if (null != function) {
            documents = function.apply(documents);
        }

        return SelectDataMapper.toMap(documents, this.poDefinition.getPoClazz(), fieldNames);
    }

    public long deleteById(MongoCollection mongoCollection, Object id) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == id) {
            return 0;
        }

        DeleteOptions deleteOptions = new DeleteOptions();
        DeleteResult deleteResult = mongoCollection.deleteOne(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id), deleteOptions);
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

    public long updateById(MongoCollection mongoCollection, Object id, Map<String, Object> values) {
        return this.updateById(mongoCollection, id, values, null);
    }

    public long updateById(
            MongoCollection mongoCollection,
            Object id,
            Map<String, Object> values,
            Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == id || RZHelper.isEmptyCollection(values)) {
            return 0;
        }

        Document document = UpdateDataMapper.toDocument(values, this.poDefinition.getPoClazz());
        if (null != function) {
            document = function.apply(document);
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(false);
        UpdateResult updateResult = mongoCollection.updateOne(
                this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)),
                new Document("$set", document),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    public long update(MongoCollection mongoCollection, Bson filter, Map<String, Object> values) {
        return this.update(mongoCollection, filter, values, null);
    }

    public long update(
            MongoCollection mongoCollection,
            Bson filter,
            Map<String, Object> values,
            Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == values || values.isEmpty()) {
            return 0;
        }

        Document document = UpdateDataMapper.toDocument(values, this.poDefinition.getPoClazz());
        if (null != function) {
            document = function.apply(document);
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        UpdateResult updateResult = mongoCollection.updateMany(
                this.formatFilter(filter),
                new Document("$set", document),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    public long updateOrInsertById(MongoCollection mongoCollection, Object id, T po) {
        return this.updateById(mongoCollection, id, po, true);
    }

    public long updateById(MongoCollection mongoCollection, Object id, T po) {
        return this.updateById(mongoCollection, id, po, false);
    }

    public long updateById(MongoCollection mongoCollection, Object id, T po, boolean isUpsert) {
        return updateById(mongoCollection, id, po, isUpsert, null);
    }

    public long updateById(
            MongoCollection mongoCollection,
            Object id,
            T po,
            boolean isUpsert,
            Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == id || null == po) {
            return 0;
        }

        Document document = UpdateDataMapper.toDocument(po);
        if (null != function) {
            document = function.apply(document);
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(isUpsert);
        UpdateResult updateResult = mongoCollection.updateOne(
                this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)),
                new Document("$set", document),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    public long update(MongoCollection mongoCollection, Bson filter, T po) {
        return this.update(mongoCollection, filter, po, null);
    }

    public long update(MongoCollection mongoCollection, Bson filter, T po, Function<Document, Document> function) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        if (null == po) {
            return 0;
        }

        Document document = UpdateDataMapper.toDocument(po);
        if (null != function) {
            document = function.apply(document);
        }

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.bypassDocumentValidation(false);
        updateOptions.upsert(false);
        UpdateResult updateResult = mongoCollection.updateMany(
                this.formatFilter(filter),
                new Document("$set", document),
                updateOptions);
        if (null == updateResult) {
            return 0;
        }

        return updateResult.getModifiedCount();
    }

    public Object increaseById(MongoCollection mongoCollection, Object id, String fieldName, int number) {
        if (null == id) {
            return null;
        }

        return this.increase(mongoCollection, this.formatFilter(Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id)), fieldName, number);
    }

    public Object increase(MongoCollection mongoCollection, Bson filter, String fieldName, int number) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotBlank(fieldName, "fieldName");

        FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();
        findOneAndUpdateOptions.upsert(false);
        findOneAndUpdateOptions.bypassDocumentValidation(false);
        Document document = (Document) mongoCollection.findOneAndUpdate(
                this.formatFilter(filter), Updates.inc(fieldName, number), findOneAndUpdateOptions);
        Map map = SelectDataMapper.toMap(document, this.poDefinition.getPoClazz(), fieldName);

        return null == map || !map.containsKey(fieldName) ? null : map.get(fieldName);
    }

    public long count(MongoCollection mongoCollection, Bson filter) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        CountOptions countOptions = new CountOptions();
        return mongoCollection.count(this.formatFilter(filter), countOptions);
    }

    public long count(MongoCollection mongoCollection) {
        Assert.isNotNull(mongoCollection, "mongoCollection");

        return mongoCollection.count();
    }

    public long countById(MongoCollection mongoCollection, Object id) {
        if (null == id) {
            return 0;
        }

        return this.count(mongoCollection, Filters.eq(PoFieldDefinition.MONGO_ID_FIELD_NAME, id));
    }

    public void createIndex(MongoCollection mongoCollection, String fieldName, boolean isAscending) {
        Assert.isNotNull(mongoCollection, "mongoCollection");
        Assert.isNotBlank(fieldName, "fieldName");
        String idFieldName = null == this.poDefinition.getIdFieldDefinition() ? null : this.poDefinition.getIdFieldDefinition().getName();
        if (!this.poDefinition.containsPoFieldDefinition(fieldName) || fieldName.equals(idFieldName)) {
            return;
        }

        ListIndexesIterable listIndexesIterable = mongoCollection.listIndexes();
        if (null == listIndexesIterable || null == listIndexesIterable.iterator()) {
            return;
        }

        Iterator documents = listIndexesIterable.iterator();
        while (documents.hasNext()) {
            Document document = (Document) documents.next();
            Map map;
            // ignore ascending or descending
            if (null != document
                    && document.containsKey("key")
                    && null != (map = (Map) document.get("key")) &&
                    map.containsKey(fieldName)) {
                return;
            }
        }

        IndexOptions indexOptions = new IndexOptions();
        indexOptions.background(true);
        mongoCollection.createIndex(isAscending ? Indexes.ascending(fieldName) : Indexes.descending(fieldName), indexOptions);
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
            mongoSorts = this.replaceIdField(mongoSorts, this.poDefinition.getPoClazz());
            MongoSortBuilder sortByBuilder = null;
            for (MongoSort mongoSort : mongoSorts) {
                if (null != mongoSort) {
                    sortByBuilder = null == sortByBuilder ? MongoSortBuilder.createByAppend(mongoSort) : sortByBuilder.append(mongoSort);
                }
            }
            findIterable.sort(Sorts.orderBy(sortByBuilder.build()));
        }

        if (null != fieldNames && 0 < fieldNames.length) {
            findIterable.projection(Projections.include(this.replaceIdField(fieldNames, this.poDefinition.getPoClazz())));
        }

        return findIterable;
    }

    private <T> List<MongoSort> replaceIdField(List<MongoSort> mongoSorts, Class<T> clazz) {
        if (null == mongoSorts) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        String idFieldName = null == this.poDefinition.getIdFieldDefinition() ? null : this.poDefinition.getIdFieldDefinition().getName();
        if (StringUtils.isBlank(idFieldName)) {
            return mongoSorts;
        }

        for (MongoSort mongoSort : mongoSorts) {
            if (null == mongoSort || StringUtils.isBlank(mongoSort.getFieldName())) {
                continue;
            }

            if (mongoSort.getFieldName().equals(idFieldName) && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(idFieldName)) {
                mongoSort.setFieldName(PoFieldDefinition.MONGO_ID_FIELD_NAME);
            }
        }

        return mongoSorts;
    }

    public <T> String[] replaceIdField(String[] fieldNames, Class<T> clazz) {
        if (null == fieldNames) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        String idFieldName = null == this.poDefinition.getIdFieldDefinition() ? null : this.poDefinition.getIdFieldDefinition().getName();
        if (StringUtils.isBlank(idFieldName)) {
            return fieldNames;
        }

        Set<String> set = new HashSet<>(Arrays.asList(fieldNames));
        if (set.contains(idFieldName) && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(idFieldName)) {
            set.add(PoFieldDefinition.MONGO_ID_FIELD_NAME);
            set.remove(idFieldName);
        }

        return set.toArray(new String[set.size()]);
    }

    private Bson formatFilter(Bson filter) {
        if (null == filter) {
            return new Document();
        } else {
            return filter;
        }
    }
}
