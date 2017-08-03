package com.rz.core.mongo.mapper;

import com.rz.core.Assert;
import com.rz.core.RZHelper;
import org.bson.Document;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renjie.zhang on 8/2/2017.
 */
public class DocumentMapper {
    private static Map<Class<?>, Field[]> fields;

    static {
        DocumentMapper.fields = new ConcurrentHashMap<>();
    }

    public static Document toDocument(Object instance) {
        if (null == instance) {
            return null;
        }
        Class<?> clazz = instance.getClass();
        if (RZHelper.isBaseClazz(clazz) || clazz.isEnum() || clazz.isArray() || instance instanceof Iterable) {
            throw new IllegalArgumentException("Class type is base or enum or array type.");
        }

        return (Document) DocumentMapper.toDocumentInternal(instance);
    }

    private static Object toDocumentInternal(Object instance) {
        if (null == instance) {
            return null;
        }

        Class<?> clazz = instance.getClass();
        if (RZHelper.isBaseClazz(clazz)) {
            return instance;
        } else if (clazz.isEnum()) {
            return ((Enum) instance).name();
        } else if (clazz.isArray()) {
            List<Object> items = new ArrayList<>();
            int length = Array.getLength(instance);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(instance, i);
                try {
                    items.add(DocumentMapper.toDocumentInternal(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return items;
        } else if (instance instanceof Iterable) {
            List<Object> items = new ArrayList<>();
            Iterator iterator = ((Iterable) instance).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                try {
                    items.add(DocumentMapper.toDocumentInternal(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return items;
        } else if (instance instanceof Map) {
            Document document = new Document();
            Map map = (Map) instance;
            for (Object item : map.entrySet()) {
                Map.Entry entry = (Map.Entry) item;

                Object entryKey = entry.getKey();
                Object newEntryKey;
                if (null == entryKey) {
                    continue;
                }
                Class<?> entryKeyClass = entryKey.getClass();
                if (RZHelper.isBaseClazz(entryKeyClass) || clazz.isEnum()) {
                    try {
                        newEntryKey = DocumentMapper.toDocumentInternal(entryKey);
                        if (null == newEntryKey) {
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                } else {
                    continue;
                }

                Object entryValue = entry.getValue();
                Object newEntryValue;
                try {
                    newEntryValue = DocumentMapper.toDocumentInternal(entryValue);
                } catch (Exception e) {
                    continue;
                }

                document.put(newEntryKey.toString(), newEntryValue);
            }

            return document;
        } else {
            Document document = new Document();
            Field[] fields;
            if (DocumentMapper.fields.containsKey(clazz)) {
                fields = RZHelper.getDeclaredFields(clazz);
                DocumentMapper.fields.put(clazz, fields);
            }
            fields = DocumentMapper.fields.get(clazz);
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);

                Object fieldValue;
                try {
                    fieldValue = field.get(instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                if (null == fieldValue) {
                    continue;
                }

                document.put(field.getName(), DocumentMapper.toDocumentInternal(fieldValue));
            }

            return document;
        }
    }

//    public static <T> T toObject(Document document, Class<T> clazz) {
//        if (null == document) {
//            return null;
//        }
//        Assert.isNotNull(clazz, "clazz");
//
//        if (RZHelper.isBaseClazz(clazz) || clazz.isEnum() || clazz.isArray() || RZHelper.interfaceOf(clazz, Iterable.class)) {
//            throw new IllegalArgumentException("Class type is base or enum or array type.");
//        }
//
//        return (T) DocumentMapper.toObjectInternal(document, clazz);
//    }
//
//    private static Object toObjectInternal(Object document, Class clazz) {
//        if (RZHelper.isBaseClazz(clazz)) {
//            return document;
//        } else if (clazz.isEnum()) {
//            return ((Enum) instance).name();
//        }
//    }
}
