package com.rz.core.utils;

import java.io.*;
import java.nio.charset.Charset;

import com.rz.core.Assert;

public class StreamUtility {
    private static final int BUFFER_SIZE = 4096;

    public static String readFileAllText(String pathName, Charset charset) throws IOException {
        Assert.isNotBlank(pathName, "pathName");

        File file = new File(pathName);
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset)) {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[StreamUtility.BUFFER_SIZE];
            int readSize = -1;
            while (-1 != (readSize = inputStreamReader.read(buffer))) {
                stringBuilder.append(buffer, 0, readSize);
            }
            return stringBuilder.toString();
        }
    }

    public static void appendFileAllText(String pathName, String contents, Charset charset) throws IOException {
        StreamUtility.writeFileAllText(pathName, contents, charset, true);
    }

    public static void writeFileAllText(String pathName, String contents, Charset charset) throws IOException {
        StreamUtility.writeFileAllText(pathName, contents, charset, false);
    }

    public static byte[] getFileBytes(String pathName) throws IOException {
        Assert.isNotBlank(pathName, "pathName");

        File file = new File(pathName);
        if (Integer.MAX_VALUE < file.length()) {
            throw new IOException("file size greater than int max value.");
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            return StreamUtility.getStreamBytes(inputStream);
        }
    }

    public static byte[] getStreamBytes(InputStream inputStream) throws IOException {
        Assert.isNotNull(inputStream, "inputStream");

        byte[] buffer = new byte[StreamUtility.BUFFER_SIZE];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int readSize = -1;
            while (0 < (readSize = inputStream.read(buffer))) {
                byteArrayOutputStream.write(buffer, 0, readSize);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }

    private static void writeFileAllText(String pathName, String contents, Charset charset, boolean append) throws IOException {
        Assert.isNotBlank(pathName, "pathName");

        File file = new File(pathName);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, append)) {
            fileOutputStream.write(contents.getBytes(charset));
        }
    }
}
