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
    private static MethodInterceptor methodInterceptor;

    @Override
    public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (Modifier.isPublic(method.getModifiers()) && !RZHelper.baseMethodNames.contains(method.getName())) {
            return ExecutantInterceptor.methodInterceptor.intercept(instance, method, args, methodProxy);
        } else {
            return methodProxy.invokeSuper(instance, args);
        }
    }

    public static void register(MethodInterceptor methodInterceptor) {
        if (null != methodInterceptor && null == ExecutantInterceptor.methodInterceptor) {
            ExecutantInterceptor.methodInterceptor = methodInterceptor;
        }
    }
}
