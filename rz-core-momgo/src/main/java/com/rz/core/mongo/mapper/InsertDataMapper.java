package com.rz.core.mongo.mapper;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rz.core.mongo.source.PoDefinition;
import com.rz.core.mongo.source.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by renjie.zhang on 8/2/2017.
 */
public class InsertDataMapper {
    public static <T> Document toDocument(T po) {
        if (null == po) {
            return null;
        }

        Class<T> clazz = (Class<T>) po.getClass();
        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        ObjectId objectId = null;
        PoFieldDefinition<T> idFieldDefinition = null == poDefinition.getIdField() ? null : poDefinition.getIdField().getItem2();
        if(null != idFieldDefinition) {
            try {
                objectId = idFieldDefinition.isObjectId() ? (ObjectId) idFieldDefinition.getValue(po) : null;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        String json = com.alibaba.fastjson.JSON.toJSONString(po, SerializerFeature.DisableCircularReferenceDetect);
        Document document = Document.parse(json);

        if(null != objectId){
            document.put(idFieldDefinition.getName(), objectId);
        }

        if (null != idFieldDefinition
                && document.containsKey(idFieldDefinition.getName())
                && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(idFieldDefinition.getName())) {
            document.put(PoFieldDefinition.MONGO_ID_FIELD_NAME, document.get(idFieldDefinition.getName()));
            document.remove(idFieldDefinition.getName());
        }

        return document;
    }
}
