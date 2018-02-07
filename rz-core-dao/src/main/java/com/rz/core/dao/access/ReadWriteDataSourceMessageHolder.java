package com.rz.core.dao.access;

/**
 * Created by Runky on 2/4/2018.
 */
public class ReadWriteDataSourceMessageHolder {
    private static ThreadLocal<DataOperationTypeEnum> dataOperationType = new ThreadLocal<>();
    private static ThreadLocal<ReadWriteDataSource> readWriteDataSource = new ThreadLocal<>();

    public static DataOperationTypeEnum getDataOperationType() {
        return ReadWriteDataSourceMessageHolder.dataOperationType.get();
    }

    public static void readable() {
        ReadWriteDataSourceMessageHolder.dataOperationType.set(DataOperationTypeEnum.READ);
    }

    public static void writeable() {
        ReadWriteDataSourceMessageHolder.dataOperationType.set(DataOperationTypeEnum.WRITE);
    }

    public static void removeDataOperationType() {
        ReadWriteDataSourceMessageHolder.dataOperationType.remove();
    }

    public static ReadWriteDataSource getReadWriteDataSource() {
        return ReadWriteDataSourceMessageHolder.readWriteDataSource.get();
    }

    public static void setReadWriteDataSource(ReadWriteDataSource readWriteDataSource) {
        ReadWriteDataSourceMessageHolder.readWriteDataSource.set(readWriteDataSource);
    }
}
