package com.rz.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.rz.core.Assert;

public class FileUtils {
    private static final int BUFFER_SIZE = 4096;

    public static String readAllText(String pathName, Charset charset) throws IOException {
        Assert.isNotBlank(pathName, "pathName");

        File file = new File(pathName);
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset)) {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[FileUtils.BUFFER_SIZE];
            int bytesRead = -1;
            while (-1 != (bytesRead = inputStreamReader.read(buffer))) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
            return stringBuilder.toString();
        }
    }

    public static void appendAllText(String pathName, String contents, Charset charset) throws IOException {
        FileUtils.writeAllText(pathName, contents, charset, true);
    }

    public static void writeAllText(String pathName, String contents, Charset charset) throws IOException {
        FileUtils.writeAllText(pathName, contents, charset, false);
    }

    private static void writeAllText(String pathName, String contents, Charset charset, boolean append) throws IOException {
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
