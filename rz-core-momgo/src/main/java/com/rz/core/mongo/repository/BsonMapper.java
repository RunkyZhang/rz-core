package com.rz.core.mongo.repository;

import com.rz.core.Assert;
import com.rz.core.mongo.builder.MongoSort;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.*;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
class BsonMapper {
    public static <T> T toObject(Document document, Class<T> clazz) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        if (document.containsKey(PoDefinition.MONGO_ID_FIELD_NAME) && !document.containsKey(poDefinition.getIdFieldName())) {
            document.put(poDefinition.getIdFieldName(), document.get("_id"));
        }
        String json = com.mongodb.util.JSON.serialize(document);

        return com.alibaba.fastjson.JSON.parseObject(json, clazz);
    }

    public static <T> List<T> toObject(Iterator<Document> documents, Class<T> clazz) {
        if (null == documents) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        List<T> pos = new ArrayList<>();
        while (documents.hasNext()) {
            T po = BsonMapper.toObject(documents.next(), clazz);
            if (null != po) {
                pos.add(po);
            }
        }

        return pos;
    }

    public static <T> Map toMap(Document document, Class<T> clazz) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        if (document.containsKey(PoDefinition.MONGO_ID_FIELD_NAME) && !document.containsKey(poDefinition.getIdFieldName())) {
            document.put(poDefinition.getIdFieldName(), document.get(PoDefinition.MONGO_ID_FIELD_NAME));
            document.remove(PoDefinition.MONGO_ID_FIELD_NAME);
        }
        String json = com.mongodb.util.JSON.serialize(document);

        return com.alibaba.fastjson.JSON.parseObject(json, Map.class);
    }

    public static <T> List<Map> toMap(Iterator<Document> documents, Class<T> clazz) {
        if (null == documents) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        List<Map> maps = new ArrayList<>();
        while (documents.hasNext()) {
            Map map = BsonMapper.toMap(documents.next(), clazz);
            if (null != map) {
                maps.add(map);
            }
        }

        return maps;
    }

    public static <T> Document toDocument(T po) {
        if (null == po) {
            return null;
        }

        PoDefinition poDefinition = SourcePool.getPoDefinition(po.getClass());

        String json = com.alibaba.fastjson.JSON.toJSONString(po);
        Document document = Document.parse(json);

        return formatId(document, po.getClass());
    }

    public static <T> List<Document> toDocument(List<T> pos) {
        if (null == pos) {
            return null;
        }

        List<Document> documents = new ArrayList<>();
        for (Object po : pos) {
            Document document = BsonMapper.toDocument(po);
            if (null != document) {
                documents.add(document);
            }
        }

        return documents;
    }

    public static <T> Document formatId(Document document, Class<T> clazz) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        if (document.containsKey(poDefinition.getIdFieldName()) && !PoDefinition.MONGO_ID_FIELD_NAME.equals(poDefinition.getIdFieldName())) {
            document.put(PoDefinition.MONGO_ID_FIELD_NAME, document.get(poDefinition.getIdFieldName()));
            document.remove(poDefinition.getIdFieldName());
        }

        return document;
    }

    public static <T> String[] formatId(String[] feildNames, Class<T> clazz) {
        if (null == feildNames) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        Set<String> set = new HashSet<>(Arrays.asList(feildNames));
        if (set.contains(poDefinition.getIdFieldName()) && !PoDefinition.MONGO_ID_FIELD_NAME.equals(poDefinition.getIdFieldName())) {
            set.add(PoDefinition.MONGO_ID_FIELD_NAME);
            set.remove(poDefinition.getIdFieldName());
        }

        return set.toArray(new String[set.size()]);
    }

    public static <T> List<MongoSort> formatId(List<MongoSort> mongoSorts, Class<T> clazz) {
        if (null == mongoSorts) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);

        for (MongoSort mongoSort : mongoSorts) {
            if (null != mongoSort && !StringUtils.isBlank(mongoSort.getFeildName())) {
                if (mongoSort.getFeildName().equals(poDefinition.getIdFieldName())
                        && !PoDefinition.MONGO_ID_FIELD_NAME.equals(poDefinition.getIdFieldName())) {
                    mongoSort.setFeildName(PoDefinition.MONGO_ID_FIELD_NAME);
                }
            }
        }

        return mongoSorts;
    }
}
