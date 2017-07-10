package com.rz.core.springbootaction.service;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
@Configuration
public class LoadConfigEnvironmentAware implements EnvironmentAware {
    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("****EnvironmentAware: " + environment);
    }
}
