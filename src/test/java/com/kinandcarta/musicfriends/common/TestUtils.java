package com.kinandcarta.musicfriends.common;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestUtils {
    public static String readFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }
}
