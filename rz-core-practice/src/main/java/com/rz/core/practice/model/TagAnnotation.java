package com.rz.core.practice.model;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TagAnnotation {
    String value();
}
