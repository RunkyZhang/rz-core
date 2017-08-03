package com.rz.core.mongo.mapper;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rz.core.Assert;
import com.rz.core.mongo.source.PoDefinition;
import com.rz.core.mongo.source.PoFieldDefinition;
import com.rz.core.mongo.source.SourcePool;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Runky on 8/2/2017.
 */
public class UpdateDataMapper {
    public static <T> Document toDocument(T po) {
        if (null == po) {
            return null;
        }

        Class<T> clazz = (Class<T>) po.getClass();
        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
        String idFieldName = null == poDefinition.getIdFieldDefinition() ? null : poDefinition.getIdFieldDefinition().getName();

        String json = com.alibaba.fastjson.JSON.toJSONString(po, SerializerFeature.DisableCircularReferenceDetect);
        Document document = Document.parse(json);

        document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
        if (!StringUtils.isBlank(idFieldName)) {
            document.remove(idFieldName);
        }

        return document;
    }

    public static <T> Document toDocument(Map<String, Object> values, Class<T> clazz) {
        if (null == values) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
        String idFieldName = null == poDefinition.getIdFieldDefinition() ? null : poDefinition.getIdFieldDefinition().getName();

        Document document = new Document();
        document.putAll(values);

        document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
        if (!StringUtils.isBlank(idFieldName)) {
            document.remove(idFieldName);
        }

        List<String> removedFieldNames =
                values.keySet().stream().filter(o -> !poDefinition.containsPoFieldDefinition(o)).collect(Collectors.toList());
        for (String removedFieldName : removedFieldNames) {
            document.remove(removedFieldName);
        }

        return document;
    }
}
