package com.rz.core.mongo.source;

import com.rz.core.Assert;
import com.rz.core.RZHelper;

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
    private PoFieldDefinition<T> idFieldDefinition;
    private List<PoFieldDefinition<T>> indexFieldDefinitions;
    private Map<String, PoFieldDefinition<T>> poFieldDefinitions;

    public Class<T> getPoClazz() {
        return this.clazz;
    }

    public PoFieldDefinition<T> getIdFieldDefinition() {
        return idFieldDefinition;
    }

    public List<PoFieldDefinition<T>> getIndexFieldDefinitions() {
        return indexFieldDefinitions;
    }

    public Map<String, PoFieldDefinition<T>> getPoFieldDefinitions() {
        return poFieldDefinitions;
    }

    public PoDefinition(Class<T> clazz) {
        Assert.isNotNull(clazz, "clazz");

        this.clazz = clazz;
        this.idFieldDefinition = null;
        this.indexFieldDefinitions = new ArrayList<>();
        this.poFieldDefinitions = new HashMap<>();

        Field[] fields = RZHelper.getDeclaredFields(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                PoFieldDefinition<T> poFieldDefinition = new PoFieldDefinition<>(clazz, field);
                if (poFieldDefinition.isIndex()) {
                    indexFieldDefinitions.add(poFieldDefinition);
                }

                if (poFieldDefinition.isId()) {
                    this.setIdField(poFieldDefinition);
                }

                this.poFieldDefinitions.put(poFieldDefinition.getName(), poFieldDefinition);
            }
        }
    }

    public boolean containsPoFieldDefinition(String name) {
        return this.poFieldDefinitions.containsKey(name);
    }

    public PoFieldDefinition<T> getPoFieldDefinition(String name) {
        return this.poFieldDefinitions.get(name);
    }

    private void setIdField(PoFieldDefinition<T> poFieldDefinition) {
        if (null == this.idFieldDefinition) {
            this.idFieldDefinition = poFieldDefinition;
        } else if (PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poFieldDefinition.getName())
                && PoFieldDefinition.ID_FIELD_NAME.equals(this.idFieldDefinition.getName())) {
            this.idFieldDefinition = poFieldDefinition;
        } else if (!PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poFieldDefinition.getName())
                && !PoFieldDefinition.ID_FIELD_NAME.equals(poFieldDefinition.getName())) {
            this.idFieldDefinition = poFieldDefinition;
        }
    }
}
