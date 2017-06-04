package com.rz.core.practice.dynamic;

import java.lang.reflect.Proxy;

import org.springframework.util.ClassUtils;

import net.sf.cglib.proxy.Enhancer;

public class CglibProxyHelper {

	public static void main(String[] args) {
		CglibProxyHelper cglibProxyHelper = new CglibProxyHelper();
		cglibProxyHelper.test();

		System.out.println("End---CglibProxyHelper");
	}

	private void test() {
		DefaultMethodInterceptor defaultMethodInterceptor = new DefaultMethodInterceptor();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Worker.class);
		enhancer.setCallback(defaultMethodInterceptor);
		Worker workerProxy = (Worker) enhancer.create();
		
		workerProxy.run(999);
		
		System.out.println("workerProxy: " + ClassUtils.isCglibProxy(workerProxy));
		
	}
}
