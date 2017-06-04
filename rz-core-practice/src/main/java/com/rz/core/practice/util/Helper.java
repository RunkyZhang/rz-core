package com.rz.core.practice.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.rz.core.practice.model.MonitorDto;
import com.rz.core.practice.model.TagAnnotation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {
    public static void main(String[] args) {

        Helper helper = new Helper();

        try {
            // helper.testClassLoader();
            helper.testLog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End Helper");
    }

    private void throwException() throws Exception {
        Thread.sleep(1000);
        throw new Exception("aasdasd");
    }

    private Object convertTest(Class<?> classType, String value) throws Exception {
        if (null == classType) {
            throw new Exception("classType");
        }
        if (null == value) {
            throw new Exception("value");
        }

        if (String.class == classType) {
            return value;
        } else if (Integer.class == classType) {
            return Integer.parseInt(value);
        } else if (Boolean.class == classType) {
            return Boolean.parseBoolean(value);
        } else if (Long.class == classType) {
            return Long.parseLong(value);
        } else if (Date.class == classType) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            return simpleDateFormat.parse(value);
        }
        // else if (true == classType.isEnum())
        // {
        // return Enum.valueOf(classType, value);
        // }
        else if (BigDecimal.class == classType) {
            return new BigDecimal(value);
        } else if (Character.class == classType) {
            return Character.valueOf(value.charAt(0));
        } else if (Short.class == classType) {
            return Short.parseShort(value);
        } else if (Byte.class == classType) {
            return Byte.parseByte(value);
        } else if (Float.class == classType) {
            return Float.parseFloat(value);
        } else if (Double.class == classType) {
            return Double.parseDouble(value);
        } else if (Byte[].class == classType) {
            return value.getBytes("UTF-8");
        } else {
            throw new Exception("NotSupported");
        }
    }

    private <T extends Enum<T>> T getEnumFromString(Class<T> classType, String value) {
        if (null != classType && null != value) {
            try {
                return Enum.valueOf(classType, value.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {

            }
        }
        return null;
    }

    // full access
    public void testPublicScope() {
        System.out.println("public");
    }

    // access for same and sub package path
    void testNothingScope() {
        System.out.println("nothing");
    }

    private ClassLoader testClassLoader() throws ClassNotFoundException, IOException {
        // return ClassUtils.getDefaultClassLoader();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(".");
        URL configUrl = classLoader.getResource("config/");
        File directory = new File(configUrl.getFile());
        System.out.println(url + "    " + StringUtils.join(directory.listFiles()));

        Class<?> clazz = classLoader.loadClass("com.rz.core.practice.io.FileHelper");
        System.out.println(clazz.toString());

        // classLoader.getFile("/config/");

        return classLoader;
    }

    // private static org.apache.logging.log4j.Logger log =
    // LogManager.getLogger(Helper.class);
    private void testLog() {
        MDC.put("requestId", "requestid1111");
        MDC.put("serverIp", "serverIp1111");

        for (int i = 0; i < 100; i++) {
            log.error("asdasdasdasd");
        }

        MDC.clear();
    }
}