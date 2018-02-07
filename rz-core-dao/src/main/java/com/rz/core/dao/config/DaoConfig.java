package com.rz.core.dao.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Runky on 2/3/2018.
 */
@Configuration
public class DaoConfig {
    @PostConstruct
    private void init() {
        System.out.println("==============================init");
    }
}
