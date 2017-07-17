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

    void insert(List<T> pos);

    List<T> selectAll();

    T selectFirst(Bson filter);

    T selectFirst(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    Map selectFirst(Bson filter, String... feildNames);

    Map selectFirst(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    T selectById(Object id);

    List<T> select(Bson filter);

    List<T> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    Map selectById(Object id, String... feildNames);

    List<Map> select(Bson filter, String... feildNames);

    List<Map> select(Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    long count();

    long countById(Object id);

    long count(Bson filter);

    long deleteById(Object id);

    long delete(Bson filter);

    long updateById(Object id, T po);

    long updateById(Object id, Map<String, Object> values);

    long update(Bson filter, Map<String, Object> values);

    long update(Bson filter, T po);
}
