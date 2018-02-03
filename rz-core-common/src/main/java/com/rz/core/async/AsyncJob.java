package com.rz.core.async;

import com.rz.core.function.ConsumerEx;

/**
 * Created by renjie.zhang on 1/11/2018.
 */
public class AsyncJob {
    private ConsumerEx<Object> consumer;
    private String name;
    private String type;
    private Object parameter;

    public ConsumerEx<Object> getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerEx<Object> consumer) {
        this.consumer = consumer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AsyncJob(){
    }

    public AsyncJob(String type, String name, ConsumerEx<Object> consumer, Object parameter){
        this.type = type;
        this.name = name;
        this.consumer = consumer;
        this.parameter = parameter;
    }
}
