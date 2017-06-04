package com.rz.core;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Assert {

    public static void isNotNull(Object value, String name) {
        if (null == value) {
            String errorMessage = "The parameter [" + name + "] value is null.";
            throw Assert.buildException(errorMessage);
        }
    }

    public static void isNotBlank(String value, String name) {
        if (StringUtils.isBlank(value)) {
            String errorMessage = "The parameter [" + name + "] value is blank.";
            throw Assert.buildException(errorMessage);
        }
    }

    public static void isNotEmpty(Collection<?> value, String name) {
        if (null == value || value.isEmpty()) {
            String errorMessage = "The parameter [" + name + "] value is empty.";
            throw Assert.buildException(errorMessage);
        }
    }

    public static void isNotEmpty(Map<?, ?> value, String name) {
        if (null == value || value.isEmpty()) {
            String errorMessage = "The parameter [" + name + "] value is empty.";
            throw Assert.buildException(errorMessage);
        }
    }

    public static void isTrue(boolean condition, String errorMessage) {
        Assert.areEqual(true, condition, errorMessage);
    }

    public static void isFalse(boolean condition, String errorMessage) {
        Assert.areEqual(false, condition, errorMessage);
    }

    public static void areEqual(Object expected, Object actual, String errorMessage) {
        if ((null == expected && null != actual) || (null != expected && !expected.equals(actual))) {
            errorMessage = null == errorMessage ? "The expected value [" + expected.toString() + "] and actual value [" + actual.toString() + "] are not equal." : errorMessage;
            throw Assert.buildException(errorMessage);
        }
    }

    public static void areNotEqual(Object expected, Object actual, String errorMessage) {
        if ((null == expected && null == actual) || (null != expected && expected.equals(actual))) {
            errorMessage = null == errorMessage ? "The expected value [" + expected.toString() + "] and actual value [" + actual.toString() + "] are equal." : errorMessage;
            throw Assert.buildException(errorMessage);
        }
    }

    private static IllegalArgumentException buildException(String errorMessage) {
        return new IllegalArgumentException(errorMessage);
    }
}
