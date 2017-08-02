package com.rz.core.mongo.builder;

import com.rz.core.Assert;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
public class MongoSort {
    private String fieldName;
    private boolean isAscending;

    public MongoSort(String fieldName, boolean isAscending) {
        Assert.isNotBlank(fieldName, "fieldName");

        this.fieldName = fieldName;
        this.isAscending = isAscending;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
