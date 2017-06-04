package com.rz.core.springaction.service;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

// need 3 packages(aspectjrt, cglib, aspectjweaver)
// aspect need match java sdk
// just for bean
// Myself is bean
// enable with @EnableAspectJAutoProxy
@Aspect
@Service
public class Audience {
	@Before("execution(public * play(..))")
	public void beforeRun(JoinPoint joinPoint){
		System.out.println("beforeRun: " + StringUtils.join(joinPoint.getArgs()));
	}
	
	@After("execution(public * play(int))")
	public void afterRun(JoinPoint joinPoint){
		System.out.println("afterRun: " + StringUtils.join(joinPoint.getArgs()));
	}
}
