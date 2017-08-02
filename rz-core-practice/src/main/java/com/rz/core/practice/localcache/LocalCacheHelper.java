package com.rz.core.practice.localcache;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

// expireAfterWrite 指定时间没有更新，就过期再次访问时同步更新返回新值。所以就算有访问，到时间也会过期
// expireAfterAccess 指定没有更新和访问，就过期再次访问时同步更新返回新值。所以只有要访问，到时间就不会过期
// refreshAfterWrite 指定时间没有更新，再次访问时就异步更新并马上返回旧值
public class LocalCacheHelper {
    public static void main(String[] args) {
        LocalCacheHelper localCacheHelper = new LocalCacheHelper();
        try {
            localCacheHelper.Test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End LocalCacheHelper...");
    }

    protected void Test() throws ExecutionException {
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
            @Override
            public String load(String arg0) throws Exception {
                // TODO Auto-generated method stub
                arg0 = null == arg0 ? "" : arg0;
                Date date = new Date();

                // cannot return null
                return arg0 + date.toString();
            }
        });

        // same time just one invoke
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(loadingCache.get("key", () -> {
                            String name = Thread.currentThread().getName();
                            System.out.println("Start: " + name);
                            Thread.sleep(5 * 1000);
                            System.out.println("End: " + name);
                            return name;
                        }));
                        Thread.sleep(5 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i)).start();
        }
    }

    protected void Test1() throws ExecutionException {
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
            @Override
            public String load(String arg0) throws Exception {
                // TODO Auto-generated method stub
                arg0 = null == arg0 ? "" : arg0;
                Date date = new Date();

                // cannot return null
                return arg0 + date.toString();
            }
        });
        System.out.println(loadingCache.get("key"));
        System.out.println(loadingCache.get("key", () -> {
            return "asdasd" + new Date().toString();
        }));
        System.out.println(loadingCache.get("key"));
        loadingCache.put("key", "value");
        System.out.println(loadingCache.get("key"));
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(loadingCache.get("key", () -> {
                return "asdasd" + new Date().toString();
            }));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
