package com.rz.core.springbootaction.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
@Component
public class BeanApplicationContextAware implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return BeanApplicationContextAware.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanApplicationContextAware.applicationContext = applicationContext;
    }
}
