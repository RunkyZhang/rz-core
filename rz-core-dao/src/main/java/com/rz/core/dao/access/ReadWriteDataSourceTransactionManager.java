package com.rz.core.dao.access;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * Created by Runky on 2/4/2018.
 */
public class ReadWriteDataSourceTransactionManager extends DataSourceTransactionManager {
    @Override
    protected void doBegin(Object transaction, TransactionDefinition transactionDefinition) {
        if (transactionDefinition.isReadOnly()) {
            DataOperationTypeHolder.set(DataOperationTypeEnum.READ);
        } else {
            DataOperationTypeHolder.set(DataOperationTypeEnum.WRITE);
        }

        super.doBegin(transaction, transactionDefinition);
    }

    /**
     * 清理本地线程的数据源
     *
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);

        DataOperationTypeHolder.remove();
    }
}
