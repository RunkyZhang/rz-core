package com.rz.core.mongo.repository;

import com.rz.core.mongo.builder.MongoSort;
import com.rz.core.mongo.source.PoDefinition;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public interface ShardingMongoRepository<TPo, TSharding> {
    PoDefinition<TPo> getPoDefinition();

    void insert(TSharding parameter, TPo po);

//    void insert(TSharding parameter, List<TPo> pos);

    List<TPo> selectAll(TSharding parameter);

    TPo selectById(TSharding parameter, Object id);

    TPo selectFirst(TSharding parameter, Bson filter);

    TPo selectFirst(TSharding parameter, Bson filter, List<MongoSort> mongoSorts);

    Map selectById(TSharding parameter, Object id, String... fieldNames);

    Map selectFirst(TSharding parameter, Bson filter, String... fieldNames);

    Object max(TSharding parameter, String fieldName);

    Object min(TSharding parameter, String fieldName);

    Map selectFirst(TSharding parameter, Bson filter, List<MongoSort> mongoSorts, String... fieldNames);

    List<TPo> selectByIds(TSharding parameter, List<Object> ids);

    List<TPo> select(TSharding parameter, Bson filter);

    List<TPo> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    List<Map> selectByIds(TSharding parameter, List<Object> ids, String... fieldNames);

    List<Map> select(TSharding parameter, Bson filter, String... fieldNames);

    List<Map> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    long deleteById(TSharding parameter, Object id);

    long delete(TSharding parameter, Bson filter);

    long updateById(TSharding parameter, Object id, Map<String, Object> values);

    long update(TSharding parameter, Bson filter, Map<String, Object> values);

    long updateOrInsertById(TSharding parameter, Object id, TPo po);

    long updateById(TSharding parameter, Object id, TPo po);

    long update(TSharding parameter, Bson filter, TPo po);

    Object increaseById(TSharding parameter, Object id, String fieldName, int number);

    Object increase(TSharding parameter, Bson filter, String fieldName, int number);

    long count(TSharding parameter);

    long countById(TSharding parameter, Object id);

    long count(TSharding parameter, Bson filter);

    void createIndex(TSharding parameter, String fieldName, boolean isAscending);

    DatabaseStatus getDatabaseStatus(TSharding parameter);
}

