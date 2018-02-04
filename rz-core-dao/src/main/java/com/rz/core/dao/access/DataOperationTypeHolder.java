package com.rz.core.dao.access;

/**
 * Created by Runky on 2/4/2018.
 */
public class DataOperationTypeHolder {
    private static ThreadLocal<DataOperationTypeEnum> dataOperationType = new ThreadLocal<>();

    public static DataOperationTypeEnum get() {
        return DataOperationTypeHolder.dataOperationType.get();
    }

    public static void set(DataOperationTypeEnum dataOperationType) {
        DataOperationTypeHolder.dataOperationType.set(dataOperationType);
    }

    public static void remove() {
        DataOperationTypeHolder.dataOperationType.remove();
    }
}
