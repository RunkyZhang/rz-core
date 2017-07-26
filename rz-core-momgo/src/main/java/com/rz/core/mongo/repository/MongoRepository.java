package com.rz.core.mongo.repository;

import com.rz.core.mongo.builder.MongoSort;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public interface MongoRepository<T> {
    PoDefinition<T> getPoDefinition();

    void insert(T po);

//    void insert(List<T> pos);

    List<T> selectAll();

    T selectById(Object id);

    T selectFirst(Bson filter);

    T selectFirst(Bson filter, List<MongoSort> mongoSorts);

    Map selectById(Object id, String... fieldNames);

    Map selectFirst(Bson filter, String... fieldNames);

    Object max(String fieldName);

    Object min(String fieldName);

    Map selectFirst(Bson filter, List<MongoSort> mongoSorts, String... fieldNames);

    List<T> selectByIds(List<Object> ids);

    List<T> select(Bson filter);

    List<T> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    List<Map> selectByIds(List<Object> ids, String... fieldNames);

    List<Map> select(Bson filter, String... fieldNames);

    List<Map> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    long deleteById(Object id);

    long delete(Bson filter);

    long updateById(Object id, Map<String, Object> values);

    long update(Bson filter, Map<String, Object> values);

    long updateOrInsertById(Object id, T po);

    long updateById(Object id, T po);

    long update(Bson filter, T po);

    Object increaseById(Object id, String fieldName, int number);

    Object increase(String fieldName, Bson filter, int number);

    long count();

    long countById(Object id);

    long count(Bson filter);

    void createIndex(String fieldName, boolean isAscending);
}
