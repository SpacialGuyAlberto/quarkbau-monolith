package com.quarkbau.monolith.infrastructure.storage;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import jakarta.annotation.PostConstruct;

@Service
public class LocalFileStorageService {

    private final Path storageFolder;
    private final String baseUrl;

    public LocalFileStorageService(
            @Value("${file.upload-dir:uploads/permits}") String uploadDir,
            @Value("${file.base-url:http://localhost:8080/api/v1/files/}") String baseUrl) {
        this.storageFolder = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder.", e);
        }
    }

    public String uploadFile(String filename, byte[] content, String contentType) {
        String key = UUID.randomUUID().toString() + "-" + filename;
        Path targetLocation = this.storageFolder.resolve(key);
        try {
            Files.write(targetLocation, content);
            return this.baseUrl + key;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + filename, ex);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.storageFolder.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found " + filename, ex);
        }
    }
}
