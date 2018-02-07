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
            ReadWriteDataSourceMessageHolder.readable();
        } else {
            ReadWriteDataSourceMessageHolder.writeable();
        }

        super.doBegin(transaction, transactionDefinition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);

        ReadWriteDataSourceMessageHolder.removeDataOperationType();
    }
}
