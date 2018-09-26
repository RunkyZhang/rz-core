package com.rz.core.dao.aop;

import com.rz.core.dao.access.ReadWriteDataSourceMessageHolder;
import org.apache.ibatis.plugin.Invocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renjie.zhang on 2/6/2018.
 */
class TransactionExecuteInterceptor extends ExecuteInterceptor {
    private Map<String, StatementAddition> statementAdditions = new ConcurrentHashMap<>();

    TransactionExecuteInterceptor(Invocation invocation) {
        super(invocation);
    }

    @Override
    Object intercept() throws Throwable {
        ReadWriteDataSourceMessageHolder.removeDataOperationType();

        return this.invocation.proceed();
    }
}
