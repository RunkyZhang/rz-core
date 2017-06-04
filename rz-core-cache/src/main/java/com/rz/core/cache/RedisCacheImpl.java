package com.rz.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.rz.core.Assert;
import com.rz.core.RZHelper;
import com.rz.core.cache.serializer.CacheSerializer;
import com.rz.core.cache.serializer.JsonCacheSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

public class RedisCacheImpl implements RedisCache {
    private static final String STATUS_CODE_OK = "OK";

    private JedisPool jedisPool;
    private CacheSerializer cacheSerializer;

    public RedisCacheImpl(String host) {
        this(host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
    }

    public RedisCacheImpl(String host, int port, int timeout, String password, int databaseId, int maxTotal) {
        this.cacheSerializer = new JsonCacheSerializer();

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);

        this.jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, databaseId);
    }

    public void setCacheSerializer(CacheSerializer cacheSerializer) {
        Assert.isNotNull(cacheSerializer, "cacheSerializer");
        this.cacheSerializer = cacheSerializer;
    }

    public CacheSerializer getCacheSerializer() {
        return this.cacheSerializer;
    }

    @Override
    public boolean stringSet(String key, Object value) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.set(SafeEncoder.encode(key), this.cacheSerializer.serialize(value)));
        });
    }

    @Override
    public boolean stringSet(String key, Object value, long expiryMillis) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.psetex(SafeEncoder.encode(key), expiryMillis, this.cacheSerializer.serialize(value)));
        });
    }

    @Override
    public boolean stringSet(Map<String, Object> keyValues) {
        Assert.isNotNull(keyValues, "keyValues");
        if (RZHelper.isEmptyCollection(keyValues)) {
            return true;
        }

        List<byte[]> parameters = new ArrayList<>();
        for (Map.Entry<String, Object> keyValue : keyValues.entrySet()) {
            if (!StringUtils.isBlank(keyValue.getKey())) {
                parameters.add(SafeEncoder.encode(keyValue.getKey()));
                parameters.add(this.cacheSerializer.serialize(keyValue.getValue()));
            }
        }

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.mset(parameters.toArray(new byte[keyValues.size() * 2][])));
        });
    }

    @Override
    public <T> T stringGet(String key, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            String value = jedis.get(key);
            return this.cacheSerializer.deserialize(value, clazz);
        });
    }

    @Override
    public <T> List<T> stringGet(Class<T> clazz, String... keys) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotNull(keys, "keys");
        if (0 == keys.length) {
            return new ArrayList<>();
        }

        return this.apply(jedis -> {
            List<String> values = jedis.mget(keys);
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public long stringIncrement(String key, long value) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.incrBy(key, value);
        });
    }

    @Override
    public long stringDecrement(String key, long value) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.decrBy(key, value);
        });
    }

    @Override
    public long hashSet(String key, String fieldName, Object value) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(fieldName, "fieldName");

        return this.apply(jedis -> {
            return jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(fieldName), this.cacheSerializer.serialize(value));
        });
    }

    @Override
    public boolean hashSet(String key, Map<String, Object> fieldNameValues) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(fieldNameValues, "fieldNameValues");
        if (RZHelper.isEmptyCollection(fieldNameValues)) {
            return true;
        }

        Map<byte[], byte[]> parameters = new HashMap<>();
        for (Map.Entry<String, Object> keyValue : fieldNameValues.entrySet()) {
            if (!StringUtils.isBlank(keyValue.getKey())) {
                parameters.put(SafeEncoder.encode(keyValue.getKey()), this.cacheSerializer.serialize(keyValue.getValue()));
            }
        }

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.hmset(SafeEncoder.encode(key), parameters));
        });
    }

    @Override
    public long hashIncrement(String key, String fieldName, long value) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(fieldName, "fieldName");

        return this.apply(jedis -> {
            return jedis.hincrBy(key, fieldName, value);
        });
    }

    @Override
    public long hashDecrement(String key, String fieldName, long value) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(fieldName, "fieldName");

        return this.apply(jedis -> {
            return jedis.hincrBy(key, fieldName, -1 * value);
        });
    }

    @Override
    public <T> T hashGet(String key, String fieldName, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(fieldName, "fieldName");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            String value = jedis.hget(key, fieldName);
            return this.cacheSerializer.deserialize(value, clazz);
        });
    }

    @Override
    public <T> List<T> hashGet(Class<T> clazz, String key, String... fieldNames) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(fieldNames, "fieldNames");
        if (0 == fieldNames.length) {
            return new ArrayList<>();
        }

        return this.apply(jedis -> {
            List<String> values = jedis.hmget(key, fieldNames);
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public <T> List<T> hashValues(String key, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            List<byte[]> values = jedis.hvals(SafeEncoder.encode(key));
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public List<String> hashFieldNames(String key) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return new ArrayList<>(jedis.hkeys(key));
        });
    }

    @Override
    public long hashDelete(String key, String... fieldNames) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(fieldNames, "fieldNames");
        if (0 == fieldNames.length) {
            return 0;
        }

        return this.apply(jedis -> {
            return jedis.hdel(key, fieldNames);
        });
    }

    @Override
    public long hashLength(String key) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.hlen(key);
        });
    }

    @Override
    public boolean hashExists(String key, String fieldName) {
        Assert.isNotBlank(key, "key");
        Assert.isNotBlank(fieldName, "fieldName");

        return this.apply(jedis -> {
            return jedis.hexists(key, fieldName);
        });
    }

    @Override
    public boolean zsetSet(String key, Object value, double score) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(value, "value");

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.zadd(SafeEncoder.encode(key), score, this.cacheSerializer.serialize(value)));
        });
    }

    @Override
    public boolean zsetSet(String key, Map<Object, Double> valueScores) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(valueScores, "valueScores");
        if (RZHelper.isEmptyCollection(valueScores)) {
            return true;
        }

        Map<byte[], Double> parameters = new HashMap<>();
        for (Map.Entry<Object, Double> valueScore : valueScores.entrySet()) {
            parameters.put(this.cacheSerializer.serialize(valueScore.getKey()), valueScore.getValue());
        }

        return this.apply(jedis -> {
            return RedisCacheImpl.STATUS_CODE_OK.equals(jedis.zadd(SafeEncoder.encode(key), parameters));
        });
    }

    @Override
    public double zsetScore(String key, Object value) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(value, "value");

        return this.apply(jedis -> {
            return jedis.zscore(SafeEncoder.encode(key), this.cacheSerializer.serialize(value));
        });
    }

    @Override
    public long zsetCount(String key, double min, double max) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.zcount(key, min, max);
        });
    }

    @Override
    public long zsetRank(String key, Object value) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(value, "value");

        return this.apply(jedis -> {
            return jedis.zrank(SafeEncoder.encode(key), this.cacheSerializer.serialize(value));
        });
    }

    @Override
    public <T> List<T> zsetRangeByRank(String key, long start, long end, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            Set<String> values = jedis.zrange(key, start, end);
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public <T> List<T> zsetRangeByScore(String key, double min, double max, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            Set<String> values = jedis.zrangeByScore(key, min, max);
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public <T> List<T> zsetRangeByScore(String key, double min, double max, int offset, int count, Class<T> clazz) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(clazz, "clazz");

        return this.apply(jedis -> {
            Set<String> values = jedis.zrangeByScore(key, min, max, offset, count);
            if (null == values) {
                return null;
            } else {
                return values.stream().map(o -> this.cacheSerializer.deserialize(o, clazz)).collect(Collectors.toList());
            }
        });
    }

    @Override
    public long zsetRemove(String key, Object... values) {
        Assert.isNotBlank(key, "key");
        Assert.isNotNull(values, "values");
        if (0 == values.length) {
            return 0;
        }

        return this.apply(jedis -> {
            return jedis.zrem(SafeEncoder.encode(key), Arrays.asList(values).stream().map(o -> this.cacheSerializer.serialize(o)).toArray(o -> new byte[values.length][]));
        });
    }

    @Override
    public long zsetRemoveRangeByRank(String key, long start, long end) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.zremrangeByRank(key, start, end);
        });
    }

    @Override
    public long zsetRemoveRangeByScore(String key, double min, double max) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.zremrangeByScore(key, min, max);
        });
    }

    @Override
    public long keyDelete(String... keys) {
        Assert.isNotNull(keys, "keys");
        if (0 == keys.length) {
            return 0;
        }

        return this.apply(jedis -> {
            return jedis.del(keys);
        });
    }

    @Override
    public long keyExists(String... keys) {
        Assert.isNotNull(keys, "keys");
        if (0 == keys.length) {
            return 0;
        }

        return this.apply(jedis -> {
            return jedis.exists(keys);
        });
    }

    @Override
    public boolean keyExpire(String key, long expiryMillis) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return 1 == jedis.pexpire(key, expiryMillis);
        });
    }

    @Override
    public boolean keyPersist(String key) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return 1 == jedis.persist(key);
        });
    }

    @Override
    public String keyType(String key) {
        Assert.isNotBlank(key, "key");

        return this.apply(jedis -> {
            return jedis.type(key);
        });
    }

    @Override
    public String keyRandom() {
        return this.apply(jedis -> {
            return jedis.randomKey();
        });
    }

    private <T> T apply(Function<Jedis, T> function) {
        try (Jedis jedis = jedisPool.getResource()) {
            return function.apply(jedis);
        }
    }
}
