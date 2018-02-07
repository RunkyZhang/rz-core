package com.rz.core.async;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by renjie.zhang on 1/11/2018.
 */
public class AsyncJobWorkerImpl implements AsyncJobWorker {
    private Semaphore semaphore;
    private Queue<AsyncJob> asyncJobs;
    private boolean disposed;
    private Thread thread;

    public AsyncJobWorkerImpl() {
        this.disposed = false;
        this.semaphore = new Semaphore(0);
        this.asyncJobs = new ConcurrentLinkedQueue<>();
    }

    public boolean isDisposed() {
        return disposed;
    }

    public synchronized AsyncJobWorker start() {
        if (null == this.thread) {
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!disposed) {
                        int size = asyncJobs.size();
                        System.out.println(String.format("There are %s jobs need to invoke.", size));

                        for (int i = 0; i < size; i++) {
                            AsyncJob job;
                            if (null != (job = asyncJobs.poll()) && null != job.getConsumer()) {
                                try {
                                    job.getConsumer().accept(job.getConsumer());
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                    System.out.println(String.format("Failed to invoke job(%s-%s).", job.getType(), job.getName()));
                                }
                            }
                        }

                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("Failed to acquire semaphore.");
                        }
                    }
                }
            });
            this.thread.start();
        }

        return this;
    }

    public boolean add(AsyncJob asyncJob) {
        if (null == asyncJob) {
            return false;
        }

        boolean result;
        try {
            result = this.asyncJobs.add(asyncJob);
            this.semaphore.release();
        } catch (Throwable throwable) {
            result = false;
            throwable.printStackTrace();
            System.out.println(String.format("Unknown exception when add async job(%s-%s).", asyncJob.getType(), asyncJob.getName()));
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        this.disposed = true;
    }
}
