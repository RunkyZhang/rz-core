package com.rz.core.practice.dynamic;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DefaultMethodInterceptor implements MethodInterceptor  {

	@Override
	public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		System.out.println(instance.getClass().getName() + "   " + methodProxy.getSuperName() + ": Start...");
		Object result = methodProxy.invokeSuper(instance, args);
		//result = method.invoke(instance, args);
		System.out.println(instance.getClass().getName() + "   " + method.getName() + ": End...");
		
		return result;
	}

}
