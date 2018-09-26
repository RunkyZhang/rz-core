package com.rz.core.async;

import com.rz.core.Assert;
import com.rz.core.Tuple2;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by renjie.zhang on 2/6/2018.
 */
public class AsyncJobTriggerImpl implements AsyncJobTrigger {
    private boolean disposed;
    private Thread thread;
    private AsyncJobWorker asyncJobWorker;
    private Queue<Tuple2<AsyncJob, Long>> asyncJobs;

    public AsyncJobTriggerImpl(AsyncJobWorker asyncWorker) {
        this(asyncWorker, null);
    }

    public AsyncJobTriggerImpl(AsyncJobWorker asyncJobWorker, AsyncJob... asyncJobs) {
        Assert.isNotNull(asyncJobWorker, "asyncJobWorker");

        this.asyncJobWorker = asyncJobWorker;
        this.asyncJobs = new ConcurrentLinkedQueue<>();
        if (null != asyncJobs) {
            for (AsyncJob asyncJob : asyncJobs) {
                if (null == asyncJob) {
                    continue;
                }

                this.asyncJobs.add(new Tuple2<>(asyncJob, 0L));
            }
        }
    }

    public void add(AsyncJob asyncJob) {
        this.asyncJobs.add(new Tuple2<>(asyncJob, 0L));
    }

    public synchronized AsyncJobTrigger start() {
        if (null == this.thread) {
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long interval = 1000;
                    while (!disposed) {
                        long timePoint = System.currentTimeMillis();

                        for (Tuple2<AsyncJob, Long> asyncJob : asyncJobs) {
                            if ((0 >= asyncJob.getItem1().getInterval() ? 10 * 1000 : asyncJob.getItem1().getInterval() * 1000)
                                    < System.currentTimeMillis() - asyncJob.getItem2()) {
                                asyncJobWorker.add(asyncJob.getItem1());
                                asyncJob.setItem2(System.currentTimeMillis());
                            }
                        }

                        long remainTime = interval - (System.currentTimeMillis() - timePoint);
                        if (0 < remainTime) {
                            try {
                                Thread.sleep(remainTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            this.thread.start();
        }

        return this;
    }

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void close() throws IOException {
        this.disposed = true;
    }
}
