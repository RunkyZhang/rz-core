package com.rz.core.practice.thread;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadHelper {
    public static void main(String[] args) {
        ThreadHelper threadHelper = new ThreadHelper();

        try {
            threadHelper.test6();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End ThreadHelper...");
    }

    // save it in main cache and thread cache
    private int notSafe;
    // save it in main cache only, so every thread can get same value.
    // [safe = 10] is atom, [safe = safe + 1] is not atom set
    private volatile int safe;
    // [threadSafe = threadSafe + 1] or [threadSafe++] is atom set
    private int threadSafe;
    // [threadSafe = threadSafe + 1] or [threadSafe++] is atom set
    private AtomicInteger threadAtomic;

    private synchronized void setIncrease() {
        this.threadSafe++;
    }

    public void setDecrement() {
        int result = threadAtomic.decrementAndGet();
    }

    private Object lock = new Object();

    public void test1() {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                synchronized (this.lock) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ": " + new Date().toString());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void test2() {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        Thread thread = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " done " + new Date().toString());
        }, "asd");
        thread.setDaemon(true);
        thread.start();

        for (int i = 0; i < count; i++) {
            System.out.println(String.valueOf(i) + ": " + new Date().toString());
            countDownLatch.countDown();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test3() {
        Semaphore semaphore = new Semaphore(0);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    semaphore.acquire(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": " + new Date().toString());
            }
        }, "asd");
        thread.start();

        for (int i = 0; i < 10; i++) {
            semaphore.release();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test4() {
        ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "default string");
        System.out.println("main start: " + threadLocal.get());
        threadLocal.set("main string");

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 start: " + threadLocal.get());
            threadLocal.set("thread1 string");
            System.out.println("thread1 start: " + threadLocal.get());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 start: " + threadLocal.get());
            threadLocal.set("thread2 string");
            System.out.println("thread2 start: " + threadLocal.get());
        });
        thread1.start();
        thread2.start();

        System.out.println("main end: " + threadLocal.get());
    }

    public void test5() throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> {
            System.out.println("done...");
        });

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "  getNumberWaiting: " + cyclicBarrier.getNumberWaiting() + "  getParties: " + cyclicBarrier.getParties()
                        + "  isBroken: " + cyclicBarrier.isBroken());
                try {
                    System.out.println(cyclicBarrier.await());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i) + "asd");
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    private void test6() {
        Phaser phaser = new Phaser(10);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "  getArrivedParties: " + phaser.getArrivedParties() + "  getRegisteredParties: "
                        + phaser.getRegisteredParties() + "  getUnarrivedParties: " + phaser.getUnarrivedParties());
                try {
                    Thread.sleep(100);
                    System.out.println(phaser.arrive());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i));
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }
}
