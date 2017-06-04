package com.rz.core.cache.serializer;

public interface CacheSerializer {
    byte[] serialize(Object value);

    <T> T deserialize(byte[] value, Class<T> clazz);

    <T> T deserialize(String value, Class<T> clazz);
}