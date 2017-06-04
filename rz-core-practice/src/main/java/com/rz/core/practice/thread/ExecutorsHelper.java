package com.rz.core.practice.thread;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorsHelper {

	public static void main(String[] args) {
		ExecutorsHelper executorsHelper = new ExecutorsHelper();

		executorsHelper.test();

		System.out.println("ExecutorsHelper");
	}

	// task time = 10, delay time = 2
	// scheduleWithFixedDelay time = task time + delay time, result 12
	// scheduleAtFixedRate time = task time > delay time ? task time : delay time, result 10
	private void test() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
		ScheduledFuture<?> scheduledFuture1 = scheduledExecutorService.scheduleWithFixedDelay(() -> {
			System.out.println("AAAStart..." + Thread.currentThread().getId() + "-----" + ": " + new Date().toString());
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
//			String issue = null;
//			System.out.println(issue.toLowerCase()); // has exception, then the task will be terminated
			
			System.out.println("AAAEnd..." + Thread.currentThread().getId() + "-----" + ": " + new Date().toString());
		}, 0, 2, TimeUnit.SECONDS);
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			System.out.println("BBBStart..." + Thread.currentThread().getId() + "-----" + ": " + new Date().toString());
			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("BBBEnd..." + Thread.currentThread().getId() + "-----" + ": " + new Date().toString());
		}, 0, 2, TimeUnit.SECONDS);

//		// cancel task
//		try {
//			Thread.sleep(1 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		scheduledFuture1.cancel(true);
//
//		scheduledExecutorService.shutdown();
//		try {
//			scheduledExecutorService.awaitTermination(20 * 1000, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	protected void test1(){
		Executors.newFixedThreadPool(10);
	}
}
