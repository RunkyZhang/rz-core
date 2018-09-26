package com.rz.core.dao.aop;

import com.rz.core.Assert;
import org.apache.ibatis.plugin.Invocation;

/**
 * Created by renjie.zhang on 2/6/2018.
 */
abstract class ExecuteInterceptor {
    protected Invocation invocation;

    ExecuteInterceptor(Invocation invocation) {
        Assert.isNotNull(invocation, "invocation");

        this.invocation = invocation;
    }

    abstract Object intercept() throws Throwable;

    static ExecuteInterceptor build(Invocation invocation) {
        Assert.isNotNull(invocation, "invocation");

        String methodName = invocation.getMethod().getName();
        if ("update".equals(methodName) || "queryCursor".equals(methodName) || "query".equals(methodName)) {
            return new OperationExecuteInterceptor(invocation);
        } else if ("update".equals(methodName) || "queryCursor".equals(methodName)) {
            return new TransactionExecuteInterceptor(invocation);
        } else {
            return null;
        }
    }
}
