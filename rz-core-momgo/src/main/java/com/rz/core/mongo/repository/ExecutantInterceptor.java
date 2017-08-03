package com.rz.core.mongo.repository;

import com.rz.core.RZHelper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Runky on 8/3/2017.
 */
public class ExecutantInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (Modifier.isPublic(method.getModifiers()) && !RZHelper.baseMethodNames.contains(method.getName())) {
            long timestamp = System.currentTimeMillis();

            System.out.println(String.format("Start: %s.", method.getName()));
            Object result = methodProxy.invokeSuper(instance, args);
            System.out.println(String.format("End(%s): %s.", System.currentTimeMillis() - timestamp, method.getName()));

            return result;
        } else {
            return methodProxy.invokeSuper(instance, args);
        }
    }
}
