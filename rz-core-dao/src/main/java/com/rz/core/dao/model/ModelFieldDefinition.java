package com.rz.core.dao.model;

import com.rz.core.Assert;
import com.rz.core.dao.annotation.DataMasking;

import javax.persistence.Id;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by renjie.zhang on 1/29/2018.
 */
public class ModelFieldDefinition {
    private Class modelClass;
    private Field field;
    private String name;
    private Method setterMethod;
    private Method getterMethod;
    private boolean dataMasking;
    private boolean primaryKey;
    private boolean canSet;
    private boolean canGet;

    public Class getModelClass() {
        return modelClass;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public boolean isDataMasking() {
        return dataMasking;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public boolean isCanSet() {
        return canSet;
    }

    public boolean isCanGet() {
        return canGet;
    }

    public ModelFieldDefinition(Class modelClass, Field field) {
        Assert.isNotNull(modelClass, "modelClass");
        Assert.isNotNull(field, "field");

        this.modelClass = modelClass;
        this.field = field;
        this.field.setAccessible(true);
        this.name = field.getName();
        this.dataMasking = false;
        this.canSet = false;
        this.canGet = false;

        try {
            this.setterMethod = new PropertyDescriptor(field.getName(), this.modelClass).getWriteMethod();
            this.setterMethod.setAccessible(true);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        try {
            this.getterMethod = new PropertyDescriptor(field.getName(), this.modelClass).getReadMethod();
            this.getterMethod.setAccessible(true);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof DataMasking) {
                this.dataMasking = true;
            }
            if (annotation instanceof Id) {
                this.primaryKey = true;
            }
        }

        if (null != this.setterMethod && Modifier.isPublic(this.setterMethod.getModifiers())) {
            this.canSet = true;
        }
        if (null != this.getterMethod && Modifier.isPublic(this.getterMethod.getModifiers())) {
            this.canGet = true;
        }
        if (Modifier.isPublic(this.field.getModifiers())) {
            this.canSet = true;
            this.canGet = true;
        }
    }

    public Object getValue(Object instance) throws InvocationTargetException, IllegalAccessException {
        Assert.isNotNull(instance, "instance");

        if (null == this.getterMethod) {
            return this.field.get(instance);
        } else {
            return this.getterMethod.invoke(instance);
        }
    }

    public void setValue(Object instance, Object value) throws IllegalAccessException, InvocationTargetException {
        Assert.isNotNull(instance, "instance");

        if (null == this.setterMethod) {
            this.field.set(instance, value);
        } else {
            this.setterMethod.invoke(instance, value);
        }
    }
}
