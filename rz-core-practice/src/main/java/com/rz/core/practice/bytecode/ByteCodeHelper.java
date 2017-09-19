package com.rz.core.practice.bytecode;

import com.rz.core.utils.StreamUtility;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

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
        StreamUtility.writeFile("E:/BCInstance.class", ByteCodeHelper.createInstance());
    }

    private static byte[] createInstance() throws IOException {
        ClassWriter classWriter = new ClassWriter(0);
        // version->java version
        classWriter.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE,
                "com.rz.core.practice.bytecode.BCInterface",
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
        // desc->type
//        classWriter.visitMethod(
//                Opcodes.ACC_PUBLIC,
//                "run",
//                "[I",
//                null,
//                new String[]{"java/io/IOException"}).visitEnd();
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
