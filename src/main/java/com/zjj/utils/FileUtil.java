package com.zjj.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static String getUploadFilePath(String filename) throws IOException {
        return getOrCreateFileRootPath() + filename;
    }


    private static String getOrCreateFileRootPath() throws IOException {
        String osName = System.getProperty("os.name");
        String rootPath = null;
        if (osName.toUpperCase().startsWith("Windows".toUpperCase())) {
            rootPath = "c:/Users/tools/file/";
        } else {
            rootPath = "~/tools/file/";
        }
        Path path = Paths.get(rootPath);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
        return rootPath;
    }
}
