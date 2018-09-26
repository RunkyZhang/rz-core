package com.rz.core.dao.access;

import com.rz.core.Assert;
import com.rz.core.dao.excpetion.DaoException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
public class ReadWriteDataSource extends AbstractRoutingDataSource {
    private DataSource[] dataSources;
    private int readRate;
    private int retryTimes;
    private Random random;
    private DataSourceSwitch dataSourceSwitch;

    public DataSourceSwitch getDataSourceSwitch() {
        return dataSourceSwitch;
    }

    public ReadWriteDataSource(DataSource... dataSources) {
        this(80, 1, dataSources);
    }

    public ReadWriteDataSource(int readRate, int retryTimes, DataSource... dataSources) {
        Assert.isNotEmpty(dataSources, "dataSources");

        this.readRate = 0 > readRate ? 0 : readRate;
        this.readRate = 100 < this.readRate ? 100 : this.readRate;
        this.retryTimes = retryTimes;
        this.random = new Random();
        this.dataSources = dataSources;
        this.dataSourceSwitch = new DataSourceSwitch(Arrays.asList(this.dataSources));
    }

    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (DataSource dataSource : dataSources) {
            targetDataSources.put(dataSource, dataSource);
        }
        this.setTargetDataSources(targetDataSources);

        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DataSource key = null;
        if (DataOperationTypeEnum.READ == ReadWriteDataSourceMessageHolder.getDataOperationType()) {
            int randomNumber = Math.abs(this.random.nextInt() % 100);
            if (randomNumber < this.readRate) {
                key = this.dataSourceSwitch.getReadDataSource(this.retryTimes);
            }
            if (null == key && 0 < this.readRate) {
                key = this.dataSourceSwitch.getWriteDataSource(this.retryTimes);
            }
        } else {
            key = this.dataSourceSwitch.getWriteDataSource(this.retryTimes);
        }

        if (null == key) {
            throw new DaoException("Cannot get valid [DataSource].");
        }

        return key;
    }
}
