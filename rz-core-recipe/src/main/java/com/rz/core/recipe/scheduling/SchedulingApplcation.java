package com.rz.core.recipe.scheduling;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SchedulingApplcation {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SchedulingConfig.class)) {
            System.out.println(StringUtils.join(applicationContext.getBeanDefinitionNames(), ","));
        }
    }
}
