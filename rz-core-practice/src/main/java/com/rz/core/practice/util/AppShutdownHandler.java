package com.rz.core.practice.util;

public class AppShutdownHandler {

    private static Thread thread;

    public static void addDefaultHandler() {
        if (null == thread) {
            AppShutdownHandler.thread = new Thread(() -> {
                System.out.println("Start shutdown.");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("End shutdown.");
            });
            
            Thread thread = new Thread(() -> {
                System.out.println("Start shutdown with exception.");
                throw new RuntimeException("666");
            });

            Runtime.getRuntime().addShutdownHook(thread);
            Runtime.getRuntime().addShutdownHook(AppShutdownHandler.thread);
        }
    }
}
