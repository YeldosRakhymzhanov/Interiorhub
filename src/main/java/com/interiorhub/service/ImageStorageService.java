package com.interiorhub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Path uploadDirectory;

    public ImageStorageService(
            @Value("${interiorhub.upload-dir:uploads}") String uploadDir) {
        this.uploadDirectory = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не выбран");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Размер изображения не должен превышать 10 МБ");
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Разрешены только изображения JPEG, PNG и WebP");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.lastIndexOf('.') >= 0) {
            extension = originalName.substring(
                    originalName.lastIndexOf('.') + 1
            ).toLowerCase();
        }

        if (!Set.of("jpg", "jpeg", "png", "webp").contains(extension)) {
            throw new IllegalArgumentException(
                    "Недопустимое расширение файла");
        }

        String filename = UUID.randomUUID() + "." + extension;

        try {
            Files.createDirectories(uploadDirectory);

            Path destination = uploadDirectory.resolve(filename).normalize();

            if (!destination.getParent().equals(uploadDirectory)) {
                throw new IllegalArgumentException("Недопустимое имя файла");
            }

            Files.copy(file.getInputStream(), destination);

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Не удалось сохранить изображение", e);
        }
    }


    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("/uploads/")) {
            return;
        }

        String filename = imageUrl.substring("/uploads/".length());

        if (filename.isBlank()
                || filename.contains("/")
                || filename.contains("\\")
                || filename.contains("..")) {
            throw new IllegalArgumentException("Недопустимое имя файла");
        }

        Path file = uploadDirectory.resolve(filename).normalize();

        if (!file.getParent().equals(uploadDirectory)) {
            throw new IllegalArgumentException("Недопустимый путь к файлу");
        }

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось удалить файл", e);
        }
    }

}
