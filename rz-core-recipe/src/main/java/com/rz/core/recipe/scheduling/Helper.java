package com.rz.core.recipe.scheduling;

import java.util.UUID;

public class Helper {
    public static String generatorUUID(String name) {
        return name + UUID.randomUUID().toString();
    }
}
