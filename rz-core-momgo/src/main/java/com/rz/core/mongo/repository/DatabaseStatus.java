package com.rz.core.mongo.repository;

import org.bson.Document;

/**
 * Created by renjie.zhang on 8/17/2017.
 */
public class DatabaseStatus {
    private String databaseName;
    private int collectionCount;
    private int viewCount;
    private int objectCount;
    private double objectAverageSize;
    private double dataSize;
    private double storageSize;
    private int numExtents;
    private int indexCount;
    private double indexSize;
    private boolean isOk;

    DatabaseStatus(Document docuemt) {
        if (null == docuemt && !docuemt.containsKey("ok")) {
            this.isOk = false;
        } else {
            this.databaseName = docuemt.containsKey("db") ? docuemt.getString("db") : null;
            this.collectionCount = docuemt.containsKey("collections") ? docuemt.getInteger("collections") : 0;
            this.viewCount = docuemt.containsKey("views") ? docuemt.getInteger("views") : 0;
            this.objectCount = docuemt.containsKey("objects") ? docuemt.getInteger("objects") : 0;
            this.objectAverageSize = docuemt.containsKey("avgObjSize") ? docuemt.getDouble("avgObjSize") : 0;
            this.dataSize = docuemt.containsKey("dataSize") ? docuemt.getDouble("dataSize") : 0;
            this.storageSize = docuemt.containsKey("storageSize") ? docuemt.getDouble("storageSize") : 0;
            this.numExtents = docuemt.containsKey("numExtents") ? docuemt.getInteger("numExtents") : 0;
            this.objectCount = docuemt.containsKey("objects") ? docuemt.getInteger("objects") : 0;
            this.indexCount = docuemt.containsKey("indexes") ? docuemt.getInteger("indexes") : 0;
            this.indexSize = docuemt.containsKey("indexSize") ? docuemt.getDouble("indexSize") : 0;
            this.isOk = 0 != docuemt.getDouble("ok");
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public double getObjectAverageSize() {
        return objectAverageSize;
    }

    public double getDataSize() {
        return dataSize;
    }

    public double getStorageSize() {
        return storageSize;
    }

    public int getNumExtents() {
        return numExtents;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public double getIndexeSize() {
        return indexSize;
    }

    public boolean isOk() {
        return isOk;
    }
}

