package com.rz.core.recipe.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.rz.core.recipe.locking.OptimisticLock;

public class Tester {
    private static OptimisticLock optimisticLock = new OptimisticLock();

    public static void main(String[] args) {
        AtomicInteger version = new AtomicInteger();
        System.out.println(version.compareAndSet(0, 6));
        
        Tester tester = new Tester();

        try {
            tester.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End Tester...");
    }

    protected void test() {
        List<Integer> parallel = new ArrayList<>();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            parallel.add(i);
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1; j++) {
                    int version = optimisticLock.getVersion();
                    if (optimisticLock.acquire(version)) {
                        System.out.println(version);
                    } else {
                        System.out.println("false");
                    }
                }
            });
        }

        parallel.parallelStream().forEach(o -> {
            threads[o].start();
        });
    }
}
