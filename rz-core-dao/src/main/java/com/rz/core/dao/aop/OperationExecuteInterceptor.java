package com.rz.core.dao.aop;

import com.rz.core.dao.access.DataOperationTypeEnum;
import com.rz.core.dao.access.ReadWriteDataSource;
import com.rz.core.dao.access.ReadWriteDataSourceMessageHolder;
import com.rz.core.dao.masking.DataMaskingTypeHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renjie.zhang on 2/6/2018.
 */
class OperationExecuteInterceptor extends ExecuteInterceptor {
    private Map<String, StatementAddition> statementAdditions = new ConcurrentHashMap<>();

    OperationExecuteInterceptor(Invocation invocation) {
        super(invocation);
    }

    @Override
    Object intercept() throws Throwable {
        Object[] objects = this.invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) objects[0];
        String mappedStatementId = mappedStatement.getId();

        if (!this.statementAdditions.containsKey(mappedStatementId)) {
            StatementAddition statementAddition = null;
            try {
                statementAddition = new StatementAddition(mappedStatement, objects[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.statementAdditions.put(mappedStatementId, statementAddition);
        }
        StatementAddition sqlAddition = statementAdditions.get(mappedStatementId);
        if (null != sqlAddition) {
            // check current is a transaction action
            boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
            if (!synchronizationActive) {
                if (DataOperationTypeEnum.READ == sqlAddition.getDataOperationType()) {
                    ReadWriteDataSourceMessageHolder.readable();
                } else {
                    ReadWriteDataSourceMessageHolder.writeable();
                }
            }

            DataMaskingTypeHandler.setParameter(sqlAddition.getTableNames(), objects[1]);
        }

        try {
            return this.invocation.proceed();
        } catch (Throwable throwable) {
            ReadWriteDataSource readWriteDataSource = ReadWriteDataSourceMessageHolder.getReadWriteDataSource();
            if (null == readWriteDataSource) {
                readWriteDataSource.getDataSourceSwitch().syncRolesAsync();
            }

            throw throwable;
        }
    }
}
