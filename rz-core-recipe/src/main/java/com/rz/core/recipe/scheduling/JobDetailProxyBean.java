package com.rz.core.recipe.scheduling;

import java.lang.reflect.Method;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution // one data share
@DisallowConcurrentExecution // wait thread finished, then start next tgread
public class JobDetailProxyBean implements Job {
    private String key;
    private String group;
    private String description;
    private String mode;
    private Tester targetObject;
    private String targetMethod;

    public String getKey() {
        return this.key;
    }

    public String getGroup() {
        return this.group;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMode() {
        return this.mode;
    }

    public Tester getTargetObject() {
        return this.targetObject;
    }

    public String getTargetMethod() {
        return this.targetMethod;
    }

    public void setTargetObject(Tester targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Method method = Tester.class.getDeclaredMethod(this.targetMethod, new Class[] {});
            method.invoke(this.targetObject, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
