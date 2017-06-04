package com.rz.core.cache.serializer;

import com.alibaba.fastjson.JSON;

import redis.clients.util.SafeEncoder;

public class JsonCacheSerializer implements CacheSerializer {
    @Override
    public byte[] serialize(Object value) {
        if (null == value) {
            return new byte[] {};
        }
        return SafeEncoder.encode(JSON.toJSONString(value));
    }

    @Override
    public <T> T deserialize(byte[] value, Class<T> clazz) {
        if (null == value) {
            return null;
        }

        return this.deserialize(SafeEncoder.encode(value), clazz);
    }
    
    @Override
    public <T> T deserialize(String value, Class<T> clazz) {
        if (null == value) {
            return null;
        }

        return JSON.parseObject(value, clazz);
    }
}
