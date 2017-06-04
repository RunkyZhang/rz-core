package com.rz.core.springaction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.rz.core.springaction.config.ApplcationConfig;
import com.rz.core.springaction.service.GamerManagaer;

public class SpringActionApplaction {
    public static void main(String[] args) {
        // ApplcationConfig is enter
        try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ApplcationConfig.class)) {
            String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                Object bean = annotationConfigApplicationContext.getBean(beanDefinitionName);
                if("gamerManagaer".equalsIgnoreCase(beanDefinitionName)){
                	System.out.println();
                	GamerManagaer gamerManagaer = (GamerManagaer) bean;
                	gamerManagaer.play();
                	gamerManagaer.play(22);
                }
                System.out.println(beanDefinitionName);
            }
        }

        System.out.println("End SpringActionApplaction...");
    }
}
