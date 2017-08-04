package com.rz.core.mongo.mapper;

import com.mongodb.MongoException;
import com.rz.core.Assert;
import com.rz.core.RZHelper;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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
        if (RZHelper.isBaseClazz(clazz)
                || clazz.isEnum()
                || clazz.equals(Date.class)
                || clazz.equals(ObjectId.class)
                || clazz.isArray()
                || instance instanceof Iterable) {
            throw new MongoException("The class type is base, enum, objectId, date, array, iterable type.");
        }

        return (Document) DocumentMapper.toDocumentInternal(instance);
    }

    private static Object toDocumentInternal(Object instance) {
        if (null == instance) {
            return null;
        }

        Class<?> clazz = instance.getClass();
        if (RZHelper.isBaseClazz(clazz) || clazz.equals(Date.class) || clazz.equals(ObjectId.class)) {
            return instance;
        } else if (clazz.isEnum()) {
            return ((Enum) instance).name();
        } else if (clazz.isArray()) {
            List<Object> items = new ArrayList<>();
            int length = Array.getLength(instance);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(instance, i);
                Object newItem = DocumentMapper.toDocumentInternal(item);
                if (null != newItem) {
                    items.add(newItem);
                }
            }

            return items;
        } else if (instance instanceof Iterable) {
            List<Object> items = new ArrayList<>();
            Iterator iterator = ((Iterable) instance).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                Object newItem = DocumentMapper.toDocumentInternal(item);
                if (null != newItem) {
                    items.add(newItem);
                }
            }

            return items;
        } else if (instance instanceof Map) {
            Document document = new Document();
            Map map = (Map) instance;
            for (Object item : map.entrySet()) {
                Map.Entry entry = (Map.Entry) item;

                if (null != entry.getKey()) {
                    Class<?> entryKeyClass = entry.getKey().getClass();
                    if (!RZHelper.isBaseClazz(entryKeyClass) && !entryKeyClass.isEnum()) {
                        throw new MongoException("The map key class type is not base or enum type.");
                    }
                    Object newEntryKey = DocumentMapper.toDocumentInternal(entry.getKey());
                    Object newEntryValue = DocumentMapper.toDocumentInternal(entry.getValue());

                    if (null != newEntryKey && null != newEntryValue) {
                        document.put(newEntryKey.toString(), newEntryValue);
                    }
                }
            }

            return document;
        } else {
            Document document = new Document();
            Field[] fields;
            if (!DocumentMapper.fields.containsKey(clazz)) {
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
                    throw new MongoException(String.format("Failed to get field(%s) value.", field.getName()), e);
                }

                if (null != fieldValue) {
                    document.put(field.getName(), DocumentMapper.toDocumentInternal(fieldValue));
                }
            }

            return document;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T toObject(Document document, Class<T> clazz) {
        if (null == document) {
            return null;
        }
        Assert.isNotNull(clazz, "clazz");

        if (RZHelper.isBaseClazz(clazz)
                || clazz.isEnum()
                || clazz.equals(Date.class)
                || clazz.equals(ObjectId.class)
                || clazz.isArray()
                || RZHelper.interfaceOf(clazz, Collection.class)) {
            throw new MongoException("The class type is base, enum, date, objectId, array, collection type.");
        }

        return (T) DocumentMapper.toObjectInternal(document, clazz, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object toObjectInternal(Object instance, Class clazz, ParameterizedType parameterizedType) {
        if (null == instance) {
            return null;
        }

        if (RZHelper.isBaseClazz(clazz) || clazz.equals(Date.class) || clazz.equals(ObjectId.class)) {
            return instance;
        } else if (RZHelper.interfaceOf(clazz, Collection.class)) {
            Collection collection = (Collection) instance;
            Collection newInstance = DocumentMapper.createCollection(clazz);
            for (Object item : collection) {
                Class genericClass = (Class) parameterizedType.getActualTypeArguments()[0];
                Object newItem = DocumentMapper.toObjectInternal(
                        item,
                        genericClass,
                        (ParameterizedType) genericClass.getGenericSuperclass());
                if (null != newItem) {
                    newInstance.add(newItem);
                }
            }

            return newInstance;
        } else if (RZHelper.interfaceOf(clazz, Map.class)) {
            Map map = (Map) instance;
            Map newInstance = DocumentMapper.createMap(clazz);

            for (Object item : map.entrySet()) {
                Map.Entry entry = (Map.Entry) item;

                if (null != entry.getKey()) {
                    Class entryKeyGenericClass = (Class) parameterizedType.getActualTypeArguments()[0];
                    Object newEntryKey = DocumentMapper.toObjectInternal(
                            entry.getKey(),
                            entryKeyGenericClass,
                            (ParameterizedType) entryKeyGenericClass.getGenericSuperclass());
                    if (null != newEntryKey) {
                        Class entryValueGenericClass = (Class) parameterizedType.getActualTypeArguments()[1];
                        Object newEntryValue = DocumentMapper.toObjectInternal(
                                entry.getValue(),
                                entryValueGenericClass,
                                (ParameterizedType) entryValueGenericClass.getGenericSuperclass());
                        newInstance.put(newEntryKey, newEntryValue);
                    }
                }
            }

            return newInstance;
        } else {
            Document document = (Document) instance;
            Object newInstance;
            try {
                newInstance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new MongoException(String.format("Failed to new instance with class(%s).", clazz.getName()), e);
            }

            Field[] fields;
            if (!DocumentMapper.fields.containsKey(clazz)) {
                fields = clazz.getDeclaredFields();
                DocumentMapper.fields.put(clazz, fields);
            }
            fields = DocumentMapper.fields.get(clazz);
            for (Field field : fields) {
                if (!document.containsKey(field.getName())) {
                    continue;
                }

                field.setAccessible(true);

                try {
                    Object newValue = DocumentMapper.toObjectInternal(
                            document.get(field.getName()),
                            field.getType(),
                            (ParameterizedType) field.getGenericType());
                    field.set(newInstance, newValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new MongoException(String.format("Failed to set field(%s) value.", field.getName()), e);
                }
            }

            return newInstance;
        }
    }

    private static Collection createCollection(Class clazz) {
        if (clazz.equals(List.class)) {
            return new ArrayList();
        } else if (clazz.equals(Set.class)) {
            return new HashSet();
        } else if (clazz.equals(Queue.class) || clazz.equals(Deque.class)) {
            return new LinkedList();
        } else if (clazz.equals(SortedSet.class) || clazz.equals(NavigableSet.class)) {
            return new TreeSet();
        } else {
            throw new MongoException("The class type fo not support to create collection instance.");
        }
    }

    private static Map createMap(Class clazz) {
        if (clazz.equals(Map.class)) {
            return new HashMap();
        } else if (clazz.equals(SortedMap.class) || clazz.equals(NavigableMap.class)) {
            return new TreeMap();
        } else {
            throw new MongoException("The class type do not support to create map instance.");
        }
    }
}
