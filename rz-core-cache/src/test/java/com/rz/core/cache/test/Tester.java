package com.rz.core.cache.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rz.core.cache.RedisCache;
import com.rz.core.cache.RedisCacheImpl;

public class Tester {
    private static RedisCache redisCache;
    private static Map<String, Object> map;

    public static void main(String[] args) {
        Tester.map = new HashMap<>();
        Tester.map.put("string", "asd");
        Tester.map.put("int", 111);
        Tester.map.put("boolean", false);
        Tester.map.put("date", new Date());

        Tester.redisCache = new RedisCacheImpl("192.168.36.212", 1008, 2000, null, 1, 10);
        Tester tester = new Tester();

        try {
            tester.testString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End Tester...");
    }

    protected void testString() {
        boolean result = false;
        result = Tester.redisCache.stringSet("rzjava", Tester.map, 10 * 60 * 1000);
        System.out.println(Tester.redisCache.stringGet("rzjava", Map.class));
        result = Tester.redisCache.stringSet(Tester.map);
        System.out.println(Tester.redisCache.stringIncrement("int", 20));
        System.out.println(Tester.redisCache.stringDecrement("int", 100));
        for (int i = 0; i < 10; i++) {
            Tester.map.put("int", i);
            Tester.redisCache.stringSet("rzjavaMap" + i, Tester.map, 10 * 60 * 1000);
        }
        // List<Map> maps = Tester.redisCache.stringGet(Map.class, "rzjavaMap1",
        // "rzjavaMap2", "rzjavaMap3", "rzjavaMap4", "rzjavaMap5");
        System.out.println(result);
    }

    protected void testHash() {

    }
}
