package com.rz.core.cache;

import java.util.List;
import java.util.Map;

public interface RedisCache {
    boolean stringSet(String key, Object value);

    boolean stringSet(String key, Object value, long expiryMillis);

    boolean stringSet(Map<String, Object> keyValues);

    long stringIncrement(String key, long value);

    long stringDecrement(String key, long value);

    <T> T stringGet(String key, Class<T> clazz);

    <T> List<T> stringGet(Class<T> clazz, String... keys);

    long hashSet(String key, String fieldName, Object value);

    boolean hashSet(String key, Map<String, Object> fieldNameValues);

    long hashIncrement(String key, String fieldName, long value);

    long hashDecrement(String key, String fieldName, long value);

    <T> T hashGet(String key, String fieldName, Class<T> clazz);

    <T> List<T> hashGet(Class<T> clazz, String key, String... fieldNames);

    <T> List<T> hashValues(String key, Class<T> clazz);

    List<String> hashFieldNames(String key);

    long hashDelete(String key, String... fieldNames);

    long hashLength(String key);

    boolean hashExists(String key, String fieldName);

    boolean zsetSet(String key, Object value, double score);

    boolean zsetSet(String key, Map<Object, Double> valueScores);

    double zsetScore(String key, Object value);

    long zsetCount(String key, double min, double max);

    long zsetRank(String key, Object value);

    <T> List<T> zsetRangeByRank(String key, long start, long end, Class<T> clazz);

    <T> List<T> zsetRangeByScore(String key, double min, double max, Class<T> clazz);
    
    <T> List<T> zsetRangeByScore(String key, double min, double max, int offset, int count, Class<T> clazz);

    long zsetRemove(String key, Object... values);

    long zsetRemoveRangeByRank(String key, long start, long end);

    long zsetRemoveRangeByScore(String key, double min, double max);

    long keyDelete(String... keys);

    long keyExists(String... keys);

    boolean keyExpire(String key, long expiryMillis);

    boolean keyPersist(String key);

    String keyType(String key);

    String keyRandom();
}
