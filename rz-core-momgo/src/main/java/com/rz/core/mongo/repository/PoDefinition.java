package com.rz.core.mongo.repository;

import com.rz.core.Assert;
import com.rz.core.RZHelper;
import com.rz.core.mongo.annotation.MongoId;
import com.rz.core.mongo.annotation.MongoIndex;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by renjie.zhang on 7/11/2017.
 */
public class PoDefinition<T> {
    public static final String ID_FIELD_NAME = "id";
    public static final String MONGO_ID_FIELD_NAME = "_id";

    private Class<T> clazz;
    private String idFieldName;
    private Set<String> indexFieldNames;

    public PoDefinition(Class<T> clazz) {
        Assert.isNotNull(clazz, "clazz");

        this.clazz = clazz;
        this.indexFieldNames = new HashSet<>();

        Field[] fields = RZHelper.getDeclaredFields(clazz);
        this.setIdFieldName(fields);
        this.setIndexFiledNames(fields);
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public String getIdFieldName() {
        return this.idFieldName;
    }

    public Set<String> getIndexFieldNames() {
        return this.indexFieldNames;
    }

    private void setIdFieldName(Field[] fields) {
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (PoDefinition.ID_FIELD_NAME.equals(field.getName())) {
                    if (StringUtils.isBlank(this.idFieldName)) {
                        this.idFieldName = PoDefinition.ID_FIELD_NAME;
                    }
                } else if (PoDefinition.MONGO_ID_FIELD_NAME.equals(field.getName())) {
                    this.idFieldName = PoDefinition.MONGO_ID_FIELD_NAME;
                }

//                if (this.isIdFieldByAnnotation(field)) {
//                    this.idFieldName = field.getName();
//                    break;
//                }
            }
        }

        if (StringUtils.isBlank(this.idFieldName)) {
            this.idFieldName = PoDefinition.MONGO_ID_FIELD_NAME;
        }
    }

    private void setIndexFiledNames(Field[] fields) {
        this.indexFieldNames.add(this.idFieldName);
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (MongoIndex.class == annotation.annotationType()) {
                    this.indexFieldNames.add(field.getName());
                }
            }
        }
    }

    private boolean isIdFieldByAnnotation(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (MongoId.class == annotation.annotationType()) {
                return true;
            }
        }

        return false;
    }
}
