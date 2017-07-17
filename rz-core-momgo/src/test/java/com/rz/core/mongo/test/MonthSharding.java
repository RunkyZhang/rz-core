package com.rz.core.mongo.test;

import com.rz.core.Assert;
import com.rz.core.mongo.repository.Shardingable;
import com.rz.core.utils.DateTimeUtility;

import java.util.Date;

/**
 * Created by renjie.zhang on 7/17/2017.
 */
public class MonthSharding implements Shardingable<Date> {
    private String rawConnectionString;
    private String rawDatabaseName;
    private String rawTableName;

    public MonthSharding(String rawConnectionString, String rawDatabaseName, String rawTableName) {

        Assert.isNotBlank(rawConnectionString, "rawConnectionString");
        Assert.isNotBlank(rawDatabaseName, "rawDatabaseName");
        Assert.isNotBlank(rawTableName, "rawTableName");

        this.rawConnectionString = rawConnectionString;
        this.rawDatabaseName = rawDatabaseName;
        this.rawTableName = rawTableName;
    }

    @Override
    public String getRawConnectionString() {
        return rawConnectionString;
    }

    @Override
    public String getRawDatabaseName() {
        return rawDatabaseName;
    }

    @Override
    public String getRawTableName() {
        return rawTableName;
    }


    @Override
    public String buildConnectionString(Date parameter) {
        return this.rawConnectionString;
    }

    @Override
    public String buildDatabaseName(Date parameter) {
        return this.rawDatabaseName;
    }

    @Override
    public String buildTableName(Date parameter) {
        Assert.isNotNull(parameter, "parameter");

        return this.rawTableName + "_" + DateTimeUtility.toString(parameter, "yyyyMM");
    }
}
