package com.rz.core.mongo.test;

import org.bson.types.ObjectId;

/**
 * Created by renjie.zhang on 7/17/2017.
 */
public class ConfigOperationLogPo extends PoBase {
    private ObjectId id;
//    private String id;
    private String applicationId;
    private String applicationVersion;
    private DataOperationTypeEnum dataOperationType;
    private ConfigNodeTypeEnum nodeType;
    private String nodeKey;
    private String nodeValue;
    private RunEnvironmentEnum runEnvironment;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public ConfigNodeTypeEnum getNodeType() {
        return nodeType;
    }

    public void setNodeType(ConfigNodeTypeEnum nodeType) {
        this.nodeType = nodeType;
    }

    public RunEnvironmentEnum getRunEnvironment() {
        return runEnvironment;
    }

    public void setRunEnvironment(RunEnvironmentEnum runEnvironment) {
        this.runEnvironment = runEnvironment;
    }

    public DataOperationTypeEnum getDataOperationType() {
        return dataOperationType;
    }

    public void setDataOperationType(DataOperationTypeEnum dataOperationType) {
        this.dataOperationType = dataOperationType;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}