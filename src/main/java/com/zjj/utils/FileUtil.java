package com.zjj.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static Path getOrCreateUploadFilePath(String filename, String md5) throws IOException {
        String dir = getOrCreateFileRootPath() + md5;
        Path path = Paths.get(dir);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return Paths.get(dir + "/" + filename);
    }


    private static String getOrCreateFileRootPath() throws IOException {
        String osName = System.getProperty("os.name");
        String rootPath = null;
        if (osName.toUpperCase().startsWith("Windows".toUpperCase())) {
            rootPath = "./";
        } else {
            rootPath = "/home/tools/file/";
        }
        Path path = Paths.get(rootPath);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return rootPath;
    }
}
