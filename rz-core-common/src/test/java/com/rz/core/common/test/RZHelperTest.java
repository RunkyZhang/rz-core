package com.rz.core.common.test;

import com.rz.core.RZHelper;

import java.io.IOException;
import java.util.List;

public class RZHelperTest {
    public static void main(String[] args) {
        RZHelperTest rzHelperTest = new RZHelperTest();
        try {
            // rzHelperTest.test();
            rzHelperTest.test2();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End HelperTest...");
    }

    public void test() throws Exception {
        String value = RZHelper.encrypt("1111111122222222".getBytes(), "asd");
        System.out.println(value);
        value = RZHelper.decrypt("1111111122222222".getBytes(), value);
        System.out.println(value);
    }

    private void test1() {
        String value = "hashCode = hashCode * seed + (int) value.charAt(i);";
        System.out.println(RZHelper.getBKDRHashCode(value));
    }

    private void test2() throws IOException {
        List<Class> classes = RZHelper.getClassesByPackage(Thread.currentThread().getContextClassLoader(), "com.rz.core.utils");
        System.out.println(classes);
    }
}
