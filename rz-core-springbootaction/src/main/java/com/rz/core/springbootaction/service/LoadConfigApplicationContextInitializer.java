package com.rz.core.springbootaction.service;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
public class LoadConfigApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("LoadConfigApplicationContextInitializer");
    }
}

