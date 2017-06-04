package com.rz.core.practice.thread;

import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;

public class QuartzHelper {
	public static void main(String[] args) {
		QuartzHelper quartzHelper = new QuartzHelper();

		try {
			quartzHelper.test();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("End QuartzHelper...");
	}

	// [second] [minutes] [hour] [day] [moth] [week] [year]
	//@Scheduled(fixedDelay = "")
	private void test() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Properties properties = new Properties();
		properties.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
		properties.setProperty("org.quartz.threadPool.threadCount", Integer.toString(10));
		((StdSchedulerFactory)schedulerFactory).initialize(properties);
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("targetObject", "getTargetObject");
        jobDataMap.put("targetMethod", "getTargetMethod()");
        jobDataMap.put("mode", "getMode");
        
		JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity("worker1").usingJobData(jobDataMap).build();
		JobDetail jobDetailNew = JobBuilder.newJob(TestJob.class).withIdentity("worker2").build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
		        .startNow()			
				.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
				//.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(2))
				.build();
		Trigger triggerNew = TriggerBuilder.newTrigger()
		        .startNow()
		        .usingJobData(jobDataMap)     			
				.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
				//.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(2))
				.build();
		
//		scheduler.addJob(jobDetail, false);
//		scheduler.addJob(jobDetailNew, false);
		
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.scheduleJob(jobDetailNew, triggerNew);
		scheduler.start();
	}
}
