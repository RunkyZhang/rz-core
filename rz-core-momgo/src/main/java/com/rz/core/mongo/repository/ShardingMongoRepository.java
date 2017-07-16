package com.rz.core.mongo.repository;

import com.rz.core.mongo.builder.MongoSort;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public interface ShardingMongoRepository<TPo, TSharding> {
    void insert(TSharding parameter, TPo po);

    void insert(TSharding parameter, List<TPo> pos);

    List<TPo> selectAll(TSharding parameter);

    TPo selectFirst(TSharding parameter, Bson filter);

    TPo selectFirst(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    Map selectFirst(TSharding parameter, Bson filter, String... fieldNames);

    Map selectFirst(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    TPo selectById(TSharding parameter, Object id);

    List<TPo> select(TSharding parameter, Bson filter);

    List<TPo> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts);

    Map selectById(TSharding parameter, Object id, String... fieldNames);

    List<Map> select(TSharding parameter, Bson filter, String... fieldNames);

    List<Map> select(TSharding parameter, Bson filter, int skip, Integer limit, List<MongoSort> mongoSorts, String... fieldNames);

    long count(TSharding parameter);

    long countById(TSharding parameter, Object id);

    long count(TSharding parameter, Bson filter);

    long deleteById(TSharding parameter, Object id);

    long delete(TSharding parameter, Bson filter);

    long updateById(TSharding parameter, Object id, TPo po);

    long updateById(TSharding parameter, Object id, Map<String, Object> values);

    long update(TSharding parameter, Bson filter, Map<String, Object> values);

    long update(TSharding parameter, Bson filter, TPo po);
}
