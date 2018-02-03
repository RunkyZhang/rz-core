package com.rz.core.practice.bytecode;

import com.rz.core.Assert;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

/**
 * Created by renjie.zhang on 9/20/2017.
 */
public class ByteCodeClassLoader extends ClassLoader {
    public static final String BCINTERFACE_NAME = "com.rz.core.practice.bytecode.BCInterface";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (ByteCodeClassLoader.BCINTERFACE_NAME.equals(name)) {
            byte[] bytes;
            try {
                bytes = this.createInstance();
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }

            return this.defineClass(name, bytes);
        }

        return super.findClass(name);
    }

    protected Class defineClass(String name, byte[] bytes) {
        Assert.isNotNull(bytes, "bytes");

        return this.defineClass(name, bytes, 0, bytes.length);
    }

    private static byte[] createInstance() throws IOException {
        ClassWriter classWriter = new ClassWriter(0);
        // version->java version
        classWriter.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE,
                ByteCodeClassLoader.BCINTERFACE_NAME,
                null,
                "java/lang/Object",
                null);
        // desc->type, value->default value
        classWriter.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "name",
                "I", // type
                null,
                66).visitEnd();
        // desc->define
        classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "compareTo",
                "(IF)Ljava/lang/String;"
                , null,
                new String[]{"java/io/IOException"}).visitEnd();

        classWriter.visitEnd();
        return classWriter.toByteArray();
    }
}
