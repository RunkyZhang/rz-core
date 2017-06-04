package com.rz.core.practice.remotecache;

//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.msgpack.MessagePack;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.hujiang.basic.framework.core.config.BaseProperties;
//import com.hujiang.basic.framework.plugin.cache.serializer.FastJson2RedisSerializer;
//import com.hujiang.basic.framework.plugin.cache.serializer.Msgpack2RedisSerializer;
//import com.hujiang.basic.framework.plugin.cache.serializer.PooledMessagePackFactory;

public class RedisHelper {
	public static void main(String[] args){
		Test();
		
		System.out.println("start...");
	}
	
	public static void Test(){
//		RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(rcf);
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		String serializerType = BaseProperties.getString("redis.serializer.type");
//		serializerType = serializerType.trim();
//		if("fastjson".equalsIgnoreCase(serializerType)){
//			redisTemplate.setValueSerializer(new FastJson2RedisSerializer<V>());
//		}else if("messagepack".equalsIgnoreCase(serializerType)){
//			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//			config.setMaxTotal(BaseProperties.getProperty("redis.messpack.pool.maxTotal", Integer.class, 500));
//			config.setMaxIdle(BaseProperties.getProperty("redis.messpack.pool.maxIdle", Integer.class, 100));
//			config.setMaxWaitMillis(BaseProperties.getProperty("redis.messpack.pool.maxWaitMillis", Long.class, 1000l));
//			GenericObjectPool<MessagePack> pool = new GenericObjectPool<MessagePack>(new PooledMessagePackFactory());
//			pool.setConfig(config);
//			redisTemplate.setValueSerializer(new Msgpack2RedisSerializer<V>(pool));
//		}
//		redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());
//		redisTemplate.setHashValueSerializer(redisTemplate.getValueSerializer());
//		log.info("redis cache set " + serializerType +" valueSerializer!");
//		redisTemplate.afterPropertiesSet();
	}
}
