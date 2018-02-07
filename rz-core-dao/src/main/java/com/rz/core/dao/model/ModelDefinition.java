package com.rz.core.dao.model;

import com.zhaogang.framework.common.Assert;
import com.zhaogang.framework.common.ZGHelper;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renjie.zhang on 1/29/2018.
 */
public class ModelDefinition {
    private Class modelClass;
    private Map<String, ModelFieldDefinition> dataMaskingFieldDefinitions;
    private Map<String, ModelFieldDefinition> fieldDefinitions;
    private Map<String, ModelFieldDefinition> idFieldDefinitions;
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public Class getModelClazz() {
        return this.modelClass;
    }

    public Map<String, ModelFieldDefinition> getDataMaskingFieldDefinitions() {
        return dataMaskingFieldDefinitions;
    }

    public Map<String, ModelFieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    public Map<String, ModelFieldDefinition> getIdFieldDefinitions() {
        return idFieldDefinitions;
    }

    public ModelDefinition(Class modelClass) {
        Assert.isNotNull(modelClass, "modelClass");

        this.modelClass = modelClass;
        this.dataMaskingFieldDefinitions = new HashMap<>();
        this.fieldDefinitions = new HashMap<>();
        this.idFieldDefinitions = new HashMap<>();

        Annotation[] annotations = modelClass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                this.tableName = ((Table) annotation).name();
            }
        }

        Field[] fields = ZGHelper.getDeclaredFields(this.modelClass);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                ModelFieldDefinition modelFieldDefinition = new ModelFieldDefinition(this.modelClass, field);
                if (modelFieldDefinition.isPrimaryKey()) {
                    this.idFieldDefinitions.put(modelFieldDefinition.getName(), modelFieldDefinition);
                }
                if (modelFieldDefinition.isDataMasking()) {
                    this.dataMaskingFieldDefinitions.put(modelFieldDefinition.getName(), modelFieldDefinition);
                }
                this.fieldDefinitions.put(modelFieldDefinition.getName(), modelFieldDefinition);
            }
        }
    }

    public boolean containsPoFieldDefinition(String name) {
        return this.fieldDefinitions.containsKey(name);
    }

    public ModelFieldDefinition getPoFieldDefinition(String name) {
        return this.fieldDefinitions.get(name);
    }
}

