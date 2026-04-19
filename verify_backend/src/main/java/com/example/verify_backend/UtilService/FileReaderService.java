package com.example.verify_backend.UtilService;


import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class FileReaderService {

    public static String readFileFromResources(String path) {

        try {
            return IOUtils.resourceToString(path, StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Не удалось прочитать ресурс: " + path, e);
        }

    }
}
