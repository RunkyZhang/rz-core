package com.rz.core.practice.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DefaultInvocationHandler implements InvocationHandler {

	private Object instance;
	
	public DefaultInvocationHandler(Object instance){
		this.instance = instance;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {		
		System.out.println(proxy.getClass().getName() + "   " + method.getName() + ": Start...");
		Object result = method.invoke(instance, args);
		System.out.println(proxy.getClass().getName() + "   " + method.getName() + ": End...");
		
		return result;
	}

}
