package com.rz.core.practice.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) {
        FutureTest futrueTest = new FutureTest();
        try {
            futrueTest.test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("End FutureTest...");
    }

    public void test1() throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return "2222";
        });
        new Thread(futureTask).start();

        new Thread(() -> {
            while (!futureTask.isDone()) {
                System.out.println("false");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("done...");
        }).start();

        System.out.println(futureTask.get());
    }

    public void test2() throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "bbbb");
        new Thread(futureTask).start();

        new Thread(() -> {
            while (!futureTask.isDone()) {
                System.out.println("false");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("done...");
        }).start();

        System.out.println(futureTask.get());
    }

    public void test3() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.toString();
    }
}
