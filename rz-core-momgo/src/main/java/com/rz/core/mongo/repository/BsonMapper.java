package com.rz.core.mongo.repository;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rz.core.Assert;
import com.rz.core.mongo.source.SourcePool;
import com.rz.core.mongo.builder.MongoSort;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
class BsonMapper {
//    public static <T> T toObject(Document document, Class<T> clazz) {
//        if (null == document) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
//
//
//        if (document.containsKey(PoFieldDefinition.MONGO_ID_FIELD_NAME) && !document.containsKey(poDefinition.getIdField().getItem1())) {
//            document.put(poDefinition.getIdField().getItem1(), document.get(PoFieldDefinition.MONGO_ID_FIELD_NAME));
//            document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
//        }
//        ObjectId objectId = null;
//        Object id = document.get(poDefinition.getIdField().getItem1());
//        if (id instanceof ObjectId) {
//            if (poDefinition.getIdField().getItem2().isObjectId()) {
//                objectId = (ObjectId) id;
//            }
//        }
//
//        String json = com.mongodb.util.JSON.serialize(document);
//
//        T po = com.alibaba.fastjson.JSON.parseObject(json, clazz);
//        if (null != objectId) {
//            try {
//                poDefinition.getIdField().getItem2().setValue(po, objectId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return po;
//    }

//    public static <T> List<T> toObject(Iterator<Document> documents, Class<T> clazz) {
//        if (null == documents) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        List<T> pos = new ArrayList<>();
//        while (documents.hasNext()) {
//            T po = BsonMapper.toObject(documents.next(), clazz);
//            if (null != po) {
//                pos.add(po);
//            }
//        }
//
//        return pos;
//    }

//    public static <T> Map toMap(Document document, Class<T> clazz, String... fieldNames) {
//        if (null == document) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//        Assert.isNotEmpty(fieldNames, "fieldNames");
//
//        Map<String, Object> map = new HashMap<>();
//        T po = BsonMapper.toObject(document, clazz);
//
//        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
//        for (String fieldName : fieldNames) {
//            if (poDefinition.containsField(fieldName)) {
//                PoFieldDefinition<T> poFieldDefinition = poDefinition.getField(fieldName);
//                if (poFieldDefinition.isCanGet()) {
//                    try {
//                        map.put(fieldName, poFieldDefinition.getValue(po));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        return map;
//    }
//
//    public static <T> List<Map> toMap(Iterator<Document> documents, Class<T> clazz, String... fieldNames) {
//        if (null == documents) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//        Assert.isNotEmpty(fieldNames, "fieldNames");
//
//        List<Map> maps = new ArrayList<>();
//        while (documents.hasNext()) {
//            Map map = BsonMapper.toMap(documents.next(), clazz, fieldNames);
//            if (null != map) {
//                maps.add(map);
//            }
//        }
//
//        return maps;
//    }

//    public static <T> Document toDocument(T po, boolean removedId) {
//        if (null == po) {
//            return null;
//        }
//
//        String json = com.alibaba.fastjson.JSON.toJSONString(po, SerializerFeature.DisableCircularReferenceDetect);
//        Document document = Document.parse(json);
//
//        return BsonMapper.formatId(document, po.getClass(), removedId);
//    }
//
//    public static <T> List<Document> toDocument(List<T> pos, boolean removedId) {
//        if (null == pos) {
//            return null;
//        }
//
//        List<Document> documents = new ArrayList<>();
//        for (Object po : pos) {
//            Document document = BsonMapper.toDocument(po, removedId);
//            if (null != document) {
//                documents.add(document);
//            }
//        }
//
//        return documents;
//    }

//    public static <T> Document toDocument(Map<String, Object> values, Class<T> clazz) {
//        Assert.isNotNull(clazz, "clazz");
//        if (null == values) {
//            return null;
//        }
//
//        Document document = new Document();
//        document.putAll(values);
//        PoDefinition poDefinition = SourcePool.getPoDefinition(clazz);
//        String idFieldName = (String) poDefinition.getIdField().getItem1();
//        document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
//        document.remove(idFieldName);
//        List<String> removedFieldNames = values.keySet().stream().filter(o -> !poDefinition.containsField(o)).collect(Collectors.toList());
//        for (String removedFieldName : removedFieldNames) {
//            document.remove(removedFieldName);
//        }
//
//        return document;
//    }

//    public static <T> Document formatId(Document document, Class<T> clazz, boolean removedId) {
//        if (null == document) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
//
//        if (removedId) {
//            document.remove(PoFieldDefinition.MONGO_ID_FIELD_NAME);
//            document.remove(poDefinition.getIdField().getItem1());
//        } else {
//            if (document.containsKey(poDefinition.getIdField().getItem1()) && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(poDefinition.getIdField().getItem1())) {
//                document.put(PoFieldDefinition.MONGO_ID_FIELD_NAME, document.get(poDefinition.getIdField().getItem1()));
//                document.remove(poDefinition.getIdField().getItem1());
//            }
//        }
//
//        return document;
//    }

//    public static <T> String[] formatId(String[] fieldNames, Class<T> clazz) {
//        if (null == fieldNames) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
//        String idFieldName = poDefinition.getIdField().getItem1();
//
//        Set<String> set = new HashSet<>(Arrays.asList(fieldNames));
//        if (set.contains(idFieldName) && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(idFieldName)) {
//            set.add(PoFieldDefinition.MONGO_ID_FIELD_NAME);
//            set.remove(poDefinition.getIdField().getItem1());
//        }
//
//        return set.toArray(new String[set.size()]);
//    }

//    public static <T> List<MongoSort> replaceIdField(List<MongoSort> mongoSorts, Class<T> clazz) {
//        if (null == mongoSorts) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        PoDefinition<T> poDefinition = SourcePool.getPoDefinition(clazz);
//        String idFieldName = poDefinition.getIdField().getItem1();
//
//        for (MongoSort mongoSort : mongoSorts) {
//            if (null == mongoSort || StringUtils.isBlank(mongoSort.getFieldName())) {
//                continue;
//            }
//
//            if (mongoSort.getFieldName().equals(idFieldName) && !PoFieldDefinition.MONGO_ID_FIELD_NAME.equals(idFieldName)) {
//                mongoSort.setFieldName(PoFieldDefinition.MONGO_ID_FIELD_NAME);
//            }
//        }
//
//        return mongoSorts;
//    }
}
