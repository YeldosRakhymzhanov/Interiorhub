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
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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

        if (!hasValidImageSignature(file)) {
            throw new IllegalArgumentException(
                    "Содержимое файла не соответствует поддерживаемому изображению");
        }

        if (!isReadableImage(file)) {
            throw new IllegalArgumentException(
                    "Изображение повреждено или его формат не поддерживается");
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

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, destination);
            }

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Не удалось сохранить изображение", e);
        }
    }


    private boolean isReadableImage(MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            BufferedImage image = ImageIO.read(input);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean hasValidImageSignature(MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            byte[] header = input.readNBytes(12);

            boolean jpeg = header.length >= 3
                    && header[0] == (byte) 0xFF
                    && header[1] == (byte) 0xD8
                    && header[2] == (byte) 0xFF;

            boolean png = header.length >= 8
                    && header[0] == (byte) 0x89
                    && header[1] == 0x50
                    && header[2] == 0x4E
                    && header[3] == 0x47
                    && header[4] == 0x0D
                    && header[5] == 0x0A
                    && header[6] == 0x1A
                    && header[7] == 0x0A;

            boolean webp = header.length >= 12
                    && header[0] == 'R'
                    && header[1] == 'I'
                    && header[2] == 'F'
                    && header[3] == 'F'
                    && header[8] == 'W'
                    && header[9] == 'E'
                    && header[10] == 'B'
                    && header[11] == 'P';

            return jpeg || png || webp;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Не удалось прочитать изображение", e);
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
