package com.rz.core.mongo.mapper;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rz.core.Assert;
import com.rz.core.mongo.repository.PoDefinition;
import com.rz.core.mongo.repository.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import org.bson.Document;

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
        PoFieldDefinition<T> idFieldDefinition = poDefinition.getIdField().getItem2();


        String json = com.alibaba.fastjson.JSON.toJSONString(po, SerializerFeature.DisableCircularReferenceDetect);
        Document document = Document.parse(json);

        if (document.containsKey(poDefinition.getIdField().getItem1())
                && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poDefinition.getIdField().getItem1())) {
            document.put(PoFieldDefinition.MONGO_ID_FIELD_NAME, document.get(poDefinition.getIdField().getItem1()));
            document.remove(poDefinition.getIdField().getItem1());
        }

        return document;
    }
}
