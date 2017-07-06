package com.rz.core.practice.dynamic;

/**
 * Created by renjie.zhang on 7/6/2017.
 */
public class InstrumentationService {
    public static String getName() {
        return "new Name";
    }

    public String getSex(boolean sex) {
        return "new " + String.valueOf(sex);
    }
}
