package com.rz.core.recipe.classagent;

import com.rz.core.utils.StreamUtility;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by renjie.zhang on 7/6/2017.
 */
public class TestClassFileTransformer implements ClassFileTransformer {
    public static String jarPathName = "C:\\Users\\renjie.zhang\\.m2\\repository\\com\\rz\\core\\rz-core-practice\\0.0.1-SNAPSHOT\\rz-core-practice-0.0.1-SNAPSHOT.jar";
    public static String className = "com/rz/core/practice/dynamic/InstrumentationService";

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (TestClassFileTransformer.className.equals(className)) {
                System.out.println((null == loader ? "NULL" : loader.getClass().getName()) + ".: " + className + "*************************");

                return TestClassFileTransformer.getJarStream(className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getJarStream(String className) throws IOException {
        JarFile jarFile = new JarFile(TestClassFileTransformer.jarPathName);
        JarEntry jarEntry = (JarEntry) jarFile.getEntry(TestClassFileTransformer.className + ".class");
        if (null == jarEntry) {
            return null;
        }

        return StreamUtility.getStreamBytes(jarFile.getInputStream(jarEntry));
    }
}
