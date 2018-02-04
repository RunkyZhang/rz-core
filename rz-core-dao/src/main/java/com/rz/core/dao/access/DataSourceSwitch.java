package com.rz.core.dao.access;

import com.rz.core.Assert;

import javax.sql.DataSource;
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


    public DataSourceSwitch(List<DataSource> dataSources) {
        Assert.isNotEmpty(dataSources, "dataSources");

        this.dataSources = dataSources;
        this.writeDataSources = new ConcurrentHashMap<>();
        this.readDataSources = new ConcurrentHashMap<>();
        this.random = new Random();
        this.syncRoles();
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

    private DataSource getWriteDataSource() {
        Object[] keys = this.writeDataSources.keySet().toArray();
        if (0 == keys.length) {
            return null;
        }

        int index = this.random.nextInt() % keys.length;
        Object key = keys[index];
        return this.writeDataSources.get(key);
    }

    private DataSource getReadDataSource() {
        Object[] keys = this.readDataSources.keySet().toArray();
        if (0 == keys.length) {
            return null;
        }

        int index = this.random.nextInt() % keys.length;
        Object key = keys[index];
        return this.readDataSources.get(key);
    }

    // error, job, get null
    private void syncRoles() {
        for (DataSource dataSource : this.dataSources) {
            Integer status = null;
            try {
                PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(DataSourceSwitch.SQL);
                ResultSet resultSet = preparedStatement.executeQuery();
                status = resultSet.getInt(DataSourceSwitch.COLUMN_LABEL);
            } catch (SQLException e) {
                e.printStackTrace();
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
    }
}
