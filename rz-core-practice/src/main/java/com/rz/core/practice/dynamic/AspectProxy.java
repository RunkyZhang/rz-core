package com.rz.core.practice.dynamic;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AspectProxy {
//    @Before("execution(public * run(..))")
//    public void beforeAttack(JoinPoint joinPoint) {
//        System.out.println("Advice: " + StringUtils.join(joinPoint.getArgs()));
//    }
    
    @Before("execution(public * run(..))")
    public void beforeAttack() {
        System.out.println("Advice: ");
    }
}
