package com.rz.core.springaction.service;

import java.lang.reflect.Method;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class BeanCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Method beanMethod = null;
        try {
            Method introspectedMethod = metadata.getClass().getMethod("getIntrospectedMethod");
            beanMethod = (Method) introspectedMethod.invoke(metadata, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isMatch = 0 == System.currentTimeMillis() % 2;
        System.out.println("java bean method is " + beanMethod.getName() + ", isMatch is " + isMatch);

        return isMatch;
    }
}
