package com.rz.core.mongo.mapper;

import com.rz.core.Assert;
import com.rz.core.mongo.source.PoDefinition;
import com.rz.core.mongo.source.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Runky on 8/2/2017.
 */
public class SelectDataMapper {
    public static <T> T toObject(Document document, Class<T> clazz) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
        PoFieldDefinition<T> idFieldDefinition = poDefinition.getIdFieldDefinition();

        ObjectId objectId = null;
        if (null != idFieldDefinition) {
            if (document.containsKey(PoFieldDefinition.MONGO_ID_FIELD_NAME) && !document.containsKey(idFieldDefinition.getName())) {
                document.put(idFieldDefinition.getName(), document.get(PoFieldDefinition.MONGO_ID_FIELD_NAME));
                document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
            }

            Object value = document.get(idFieldDefinition.getName());
            if (value instanceof ObjectId) {
                if (idFieldDefinition.isObjectId()) {
                    objectId = (ObjectId) value;
                }
            }
        }

        String json = com.mongodb.util.JSON.serialize(document);
        T po = com.alibaba.fastjson.JSON.parseObject(json, clazz);

        if (null != objectId) {
            try {
                idFieldDefinition.setValue(po, objectId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return po;
    }

    public static <T> List<T> toObject(Iterator<Document> documents, Class<T> clazz) {
        if (null == documents) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        List<T> pos = new ArrayList<>();
        while (documents.hasNext()) {
            T po = SelectDataMapper.toObject(documents.next(), clazz);
            if (null != po) {
                pos.add(po);
            }
        }

        return pos;
    }

    public static <T> Map toMap(Document document, Class<T> clazz, String... fieldNames) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        Map<String, Object> map = new HashMap<>();
        T po = SelectDataMapper.toObject(document, clazz);

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
        for (String fieldName : fieldNames) {
            if (poDefinition.containsPoFieldDefinition(fieldName)) {
                PoFieldDefinition<T> poFieldDefinition = poDefinition.getPoFieldDefinition(fieldName);
                try {
                    map.put(fieldName, poFieldDefinition.getValue(po));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    public static <T> List<Map> toMap(Iterator<Document> documents, Class<T> clazz, String... fieldNames) {
        if (null == documents) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotEmpty(fieldNames, "fieldNames");

        List<Map> maps = new ArrayList<>();
        while (documents.hasNext()) {
            Map map = SelectDataMapper.toMap(documents.next(), clazz, fieldNames);
            if (null != map) {
                maps.add(map);
            }
        }

        return maps;
    }
}
