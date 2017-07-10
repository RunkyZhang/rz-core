package com.rz.core.springbootaction.service;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
public class PropertyApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        System.out.println("****ApplicationListener: " + applicationEnvironmentPreparedEvent.getEnvironment());
    }
}

