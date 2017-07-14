package com.rz.core.mongo.builder;

import com.rz.core.Assert;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
public class MongoSort {
    private String feildName;
    private boolean isAscending;

    public MongoSort(String feildName, boolean isAscending) {
        Assert.isNotBlank(feildName, "feildName");

        this.feildName = feildName;
        this.isAscending = isAscending;
    }

    public String getFeildName() {
        return feildName;
    }

    public void setFeildName(String feildName) {
        this.feildName = feildName;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }
}
