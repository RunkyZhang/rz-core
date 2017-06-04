package com.rz.core.practice.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
    public static void main(String[] args) {
        LockTest reentrantLockTest = new LockTest();
        try {
            reentrantLockTest.test6();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End ReentrantLockTest...");
    }

    public void test() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                reentrantLock.lock();
                try {
                    System.out.println(System.currentTimeMillis() + ": asd");
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                    }
                } finally {
                    reentrantLock.unlock();
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    public void test1() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                boolean flag = false;
                try {
                    flag = reentrantLock.tryLock(5 * 1000, TimeUnit.MILLISECONDS);
                    System.out.println(System.currentTimeMillis() + ": asd");
                    Thread.sleep(1 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reentrantLock.isLocked() || flag) {
                        reentrantLock.unlock();
                    }
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    int value = 0;

    public void test2() {
        Lock lock = new ReentrantLock();
        // only use one condition is OK
        Condition conditionFrist = lock.newCondition();
        Condition conditionLast = lock.newCondition();

        Thread threadFrist = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("----1-Frsit in lock...");

                while (5 > value) {
                    value++;
                    System.out.println("1-Frsit: " + value);
                    Thread.sleep(200);
                }

                conditionLast.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            try {
                lock.lock();
                System.out.println("----2-Frsit in lock...");

                if (15 > value) {
                    // when await, anther can enter lock
                    conditionFrist.await();
                }

                while (15 > value) {
                    value++;
                    System.out.println("2-Frsit: " + value);
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread threadLast = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("----1-Last in lock...");

                if (5 > value) {
                    // when await, anther can enter lock
                    conditionLast.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            try {
                lock.lock();
                System.out.println("----2-Last in lock...");

                while (10 > value) {
                    value++;
                    System.out.println("Last: " + value);
                    Thread.sleep(200);
                }

                conditionFrist.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        threadLast.start();
        System.out.println("----Last started...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadFrist.start();
        System.out.println("----Frsit started...");
    }

    public void test3() {
        Lock lock = new ReentrantLock();
        // use tow conditions is OK
        Condition condition = lock.newCondition();

        Thread threadFrist = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("----Frsit in lock...");

                while (5 > value) {
                    value++;
                    System.out.println("1-Frsit: " + value);
                    Thread.sleep(200);
                }

                condition.signal();

                if (15 > value) {
                    condition.await();
                }

                while (15 > value) {
                    value++;
                    System.out.println("2-Frsit: " + value);
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread threadLast = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("----Last in lock...");

                if (5 > value) {
                    // when await, anther can enter lock
                    condition.await();
                }

                while (10 > value) {
                    value++;
                    System.out.println("Last: " + value);
                    Thread.sleep(200);
                }

                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        threadLast.start();
        System.out.println("----Last started...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadFrist.start();
        System.out.println("----Frsit started...");
    }

    public void test4() {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                synchronized (this) {
                    System.out.println(System.currentTimeMillis() + ": asd");
                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    public void test5() {
        Object lock = new Object();

        Thread threadFrist = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("----Frsit in lock...");

                    while (5 > value) {
                        value++;
                        System.out.println("1-Frsit: " + value);
                        Thread.sleep(200);
                    }

                    // release all await
                    lock.notifyAll();

                    if (15 > value) {
                        lock.wait();
                    }

                    while (15 > value) {
                        value++;
                        System.out.println("2-Frsit: " + value);
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        Thread threadLast = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("----Last in lock...");

                    if (5 > value) {
                        // when await, anther can enter lock
                        lock.wait();
                    }

                    while (10 > value) {
                        value++;
                        System.out.println("Last: " + value);
                        Thread.sleep(200);
                    }

                    // release one await
                    lock.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadLast.start();
        System.out.println("----Last started...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadFrist.start();
        System.out.println("----Frsit started...");
    }

    public void test6() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock readLock = readWriteLock.readLock();
        Lock writeLock = readWriteLock.writeLock();

        Thread thread1 = new Thread(() -> {
            while (100 > value) {
                try {
                    readLock.lock();
                    System.out.println("thread1-read: " + value);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (50 == value) {
                        try {
                            System.out.println("thread1-read: wait unlock");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    readLock.unlock();
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                readLock.lock();
                while (50 > value) {
                    value++;
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }

            try {
                writeLock.lock();
                System.out.println("thread2-write: enter");
                while (100 > value) {
                    value++;
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        });
        thread1.start();
        thread2.start();
    }
}
