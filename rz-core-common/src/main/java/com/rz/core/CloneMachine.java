package com.rz.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CloneMachine {
    private static Map<Class<?>, Field[]> fields;

    static {
        CloneMachine.fields = new ConcurrentHashMap<>();
    }

    public static <T> T clone(T instance) throws Exception {
        Set<Object> instanceRecords = new HashSet<>();
        return CloneMachine.clone(instance, instanceRecords, true);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> T clone(T instance, Set<Object> instanceRecords, boolean ignoreException) throws Exception {
        if (null == instance) {
            return null;
        }

        // not allow loop reference the same instance.
        // throw StackOverflowError when invoke .hashCode() with by instance that use @Data.
        // that has loop reference.
        if (instanceRecords.contains(instance)) {
            return instance;
        } else {
            instanceRecords.add(instance);
        }

        Class clazz = instance.getClass();

        if (RZHelper.isBaseClazz(clazz) || clazz.isEnum()) {
            return instance;
        }

        T newInstance;
        int length = 0;
        try {
            if (clazz.isArray()) {
                length = Array.getLength(instance);
                newInstance = (T) Array.newInstance(clazz.getComponentType(), length);
            } else {
                newInstance = (T) clazz.newInstance();
            }
        } catch (Exception e) {
            if (ignoreException) {
                System.out.println(e.getMessage());
            } else {
                throw e;
            }

            return instance;
        }

        if (clazz.isArray()) {
            for (int i = 0; i < length; i++) {
                Object item = Array.get(instance, i);
                Array.set(newInstance, i, CloneMachine.safeClone(item, instanceRecords, ignoreException));
            }
        } else if (instance instanceof Collection) {
            Collection collection = (Collection) instance;
            for (Object item : collection) {
                ((Collection) newInstance).add(CloneMachine.safeClone(item, instanceRecords, ignoreException));
            }
        } else if (instance instanceof Map) {
            Map map = (Map) instance;
            for (Object item : map.entrySet()) {
                Map.Entry entry = (Map.Entry) item;

                ((Map) newInstance).put(
                        CloneMachine.safeClone(entry.getKey(), instanceRecords, ignoreException),
                        CloneMachine.safeClone(entry.getValue(), instanceRecords, ignoreException));
            }
        } else {
            Field[] fields;
            if (!CloneMachine.fields.containsKey(clazz)) {
                fields = clazz.getDeclaredFields();
                CloneMachine.fields.put(clazz, fields);
            }
            fields = CloneMachine.fields.get(clazz);
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                // TODO Annotation check
                // Annotation[] annotations= field.getAnnotations();

                field.setAccessible(true);
                Object fieldValue = field.get(instance);

                field.set(newInstance, CloneMachine.safeClone(fieldValue, instanceRecords, ignoreException));
            }
        }

        return newInstance;
    }

    private static <T> T safeClone(T instance, Set<Object> instanceRecords, boolean ignoreException) throws Exception {
        try {
            return CloneMachine.clone(instance, instanceRecords, ignoreException);
        } catch (Exception e) {
            if (ignoreException) {
                System.out.println(e.getMessage());
                return instance;
            } else {
                throw e;
            }
        }
    }
}
