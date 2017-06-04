package com.rz.core.practice.thread;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution // one data share
@DisallowConcurrentExecution // wait thread finished, then start next tgread
public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Start..." + Thread.currentThread().getId() + "-----" + context.getJobDetail().getKey().getName() + ": "
				+ new Date().toString());
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End..." + Thread.currentThread().getId() + "-----" + context.getJobDetail().getKey().getName() + ": "
				+ new Date().toString());
	}
}
