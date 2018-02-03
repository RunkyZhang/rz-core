package com.rz.core.practice.bytecode;

/**
 * Created by Runky on 9/16/2017.
 * boolean Z
 * char C
 * byte B
 * short S
 * int I
 * float F
 * long J
 * double D
 * Object Ljava/lang/Object;
 * int[] [I
 * Object[][] [[Ljava/lang/Object;
 * <p>
 * void m(int i, float f) (IF)V
 * int m(Object o) (Ljava/lang/Object;)I
 * int[] m(int i, String s) (ILjava/lang/String;)[I
 * Object m(int[] i) ([I)Ljava/lang/Object;
 */
public class ByteCodeHelper {
    public static void main(String[] args) throws Exception {
        Thread.currentThread().setContextClassLoader(new ByteCodeClassLoader());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class clazz = classLoader.loadClass(ByteCodeClassLoader.BCINTERFACE_NAME);
//        StreamUtility.writeFile("D:/BCInstance.class", ByteCodeHelper.createInstance());
    }
}
