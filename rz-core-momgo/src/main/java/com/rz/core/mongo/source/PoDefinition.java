package com.rz.core.mongo.source;

import com.rz.core.Assert;
import com.rz.core.RZHelper;
import com.rz.core.Tuple2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/11/2017.
 */
public class PoDefinition<T> {
    private Class<T> clazz;
    private Tuple2<String, PoFieldDefinition<T>> idField;
    private List<Tuple2<String, PoFieldDefinition<T>>> indexFields;
    private Map<String, PoFieldDefinition<T>> fields;

    public Class<T> getPoClazz() {
        return this.clazz;
    }

    public Tuple2<String, PoFieldDefinition<T>> getIdField() {
        return idField;
    }

    public List<Tuple2<String, PoFieldDefinition<T>>> getIndexFields() {
        return indexFields;
    }

    public Map<String, PoFieldDefinition<T>> getFields() {
        return fields;
    }

    public PoDefinition(Class<T> clazz) {
        Assert.isNotNull(clazz, "clazz");

        this.clazz = clazz;
        this.idField = null;
        this.indexFields = new ArrayList<>();
        this.fields = new HashMap<>();

        Field[] fields = RZHelper.getDeclaredFields(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                PoFieldDefinition<T> poFieldDefinition = new PoFieldDefinition<>(clazz, field);
                if (poFieldDefinition.isIndex()) {
                    indexFields.add(new Tuple2<>(poFieldDefinition.getName(), poFieldDefinition));
                }

                if (poFieldDefinition.isId()) {
                    this.setIdField(poFieldDefinition);
                }

                this.fields.put(poFieldDefinition.getName(), poFieldDefinition);
            }
        }

        if (null == idField) {
            this.idField = new Tuple2<>(PoFieldDefinition.MONGO_ID_FIELD_NAME, null);
        }
    }

    public boolean containsField(String name) {
        return this.fields.containsKey(name);
    }

    public PoFieldDefinition<T> getField(String name) {
        return this.fields.get(name);
    }

    public void getFieldValue(String name) {
        this.fields.get(name);
    }

    private void setIdField(PoFieldDefinition<T> poFieldDefinition) {
        if (null == idField) {
            this.idField = new Tuple2<>(poFieldDefinition.getName(), poFieldDefinition);
        } else if (PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poFieldDefinition.getName()) && PoFieldDefinition.ID_FIELD_NAME.equals(this.idField.getItem1())) {
            this.idField = new Tuple2<>(poFieldDefinition.getName(), poFieldDefinition);
        } else if (!PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poFieldDefinition.getName()) && !PoFieldDefinition.ID_FIELD_NAME.equals(poFieldDefinition.getName())) {
            this.idField = new Tuple2<>(poFieldDefinition.getName(), poFieldDefinition);
        }
    }
}
