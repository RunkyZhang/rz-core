package com.rz.core.dao.access;

import com.zhaogang.framework.common.Assert;
import com.zhaogang.framework.common.async.AsyncJob;
import com.zhaogang.framework.common.function.ConsumerEx;
import com.zhaogang.framework.dal.SourcePool;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renjie.zhang on 2/2/2018.
 */
public class DataSourceSwitch {
    private static final String COLUMN_LABEL = "status";
    private static final String SQL = "SELECT @@read_only AS " + DataSourceSwitch.COLUMN_LABEL;
    private List<DataSource> dataSources;
    private Map<DataSource, DataSource> writeDataSources;
    private Map<DataSource, DataSource> readDataSources;
    private Random random;
    private AsyncJob asyncJob;
    private long syncRolesTimePoint;

    public DataSourceSwitch(List<DataSource> dataSources) {
        Assert.isNotEmpty(dataSources, "dataSources");

        this.dataSources = dataSources;
        this.writeDataSources = new ConcurrentHashMap<>();
        this.readDataSources = new ConcurrentHashMap<>();
        this.random = new Random();
        this.syncRoles();

        SourcePool.asyncJobWorker.start();
        SourcePool.asyncJobTrigger.start();
        this.asyncJob = new AsyncJob(
                DataSourceSwitch.class.getSimpleName(),
                String.valueOf(this.hashCode()),
                new ConsumerEx<Object>() {
                    @Override
                    public void accept(Object o) throws Throwable {
                        syncRoles();
                    }
                },
                null,
                5);
        SourcePool.asyncJobTrigger.add(asyncJob);
    }

    public DataSource getWriteDataSource(int retryTimes) {
        retryTimes = 1 > retryTimes ? 1 : retryTimes;
        DataSource dataSource = null;
        for (int i = 0; i <= retryTimes; i++) {
            dataSource = this.getWriteDataSource();
            if (null == dataSource) {
                this.syncRoles();
            } else {
                break;
            }
        }

        return dataSource;
    }

    public DataSource getReadDataSource(int retryTimes) {
        retryTimes = 1 > retryTimes ? 1 : retryTimes;
        DataSource dataSource = null;
        for (int i = 0; i <= retryTimes; i++) {
            dataSource = this.getReadDataSource();
            if (null == dataSource) {
                this.syncRoles();
            } else {
                break;
            }
        }

        return dataSource;
    }

    public void syncRolesAsync() {
        SourcePool.asyncJobWorker.add(this.asyncJob);
    }

    private DataSource getWriteDataSource() {
        Object[] keys = this.writeDataSources.keySet().toArray();
        if (0 == keys.length) {
            return null;
        }

        int index = Math.abs(this.random.nextInt() % keys.length);
        Object key = keys[index];
        return this.writeDataSources.get(key);
    }

    private DataSource getReadDataSource() {
        Object[] keys = this.readDataSources.keySet().toArray();
        if (0 == keys.length) {
            return null;
        }

        int index = Math.abs(this.random.nextInt() % keys.length);
        Object key = keys[index];
        return this.readDataSources.get(key);
    }

    // be invoked when get null or job or error
    private synchronized void syncRoles() {
        if (10 < System.currentTimeMillis() - this.syncRolesTimePoint) {
            for (DataSource dataSource : this.dataSources) {
                Integer status = null;
                Connection connection = DataSourceUtils.getConnection(dataSource);
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = connection.prepareStatement(DataSourceSwitch.SQL);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        status = resultSet.getInt(DataSourceSwitch.COLUMN_LABEL);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Failed to sync database role.");
                } finally {
                    JdbcUtils.closeStatement(preparedStatement);
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }

                if (null == status) {
                    this.readDataSources.remove(dataSource);
                    this.writeDataSources.remove(dataSource);
                } else if (0 == status) {
                    this.readDataSources.remove(dataSource);
                    this.writeDataSources.put(dataSource, dataSource);
                } else {
                    this.writeDataSources.remove(dataSource);
                    this.readDataSources.put(dataSource, dataSource);
                }
            }

            this.syncRolesTimePoint = System.currentTimeMillis();
        }
    }
}
