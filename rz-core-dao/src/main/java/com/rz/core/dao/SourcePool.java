package com.rz.core.dao;

import com.rz.core.dao.masking.DataMasker;
import com.rz.core.dao.model.ModelDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
public class SourcePool {
    private static Map<String, ModelDefinition> modelDefinitions = new HashMap<>();// need support Regular Expression
    public static Map<String, DataMasker> dataMaskers = new HashMap<>();
    public static AsyncJobWorker asyncJobWorker = new AsyncJobWorkerImpl();
    public static AsyncJobTrigger asyncJobTrigger = new AsyncJobTriggerImpl(SourcePool.asyncJobWorker);

    public static ModelDefinition getModelDefinition(String tableName) {
        tableName = null == tableName ? null : tableName.toLowerCase();

        return SourcePool.modelDefinitions.get(tableName);
    }

    public static ModelDefinition addModelDefinition(String tableName, ModelDefinition modelDefinition) {
        tableName = null == tableName ? null : tableName.toLowerCase();

        return SourcePool.modelDefinitions.put(tableName, modelDefinition);
    }

    public static boolean containsModelDefinitionKey(String tableName) {
        tableName = null == tableName ? null : tableName.toLowerCase();

        return SourcePool.modelDefinitions.containsKey(tableName);
    }
}
