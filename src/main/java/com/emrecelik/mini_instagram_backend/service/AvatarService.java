package com.emrecelik.mini_instagram_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarService {

    @Value("${app.uploads.dir}")
    private String uploadDir;

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    public String saveAvatar(MultipartFile file, Long userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        String contentType = Objects.requireNonNull(file.getContentType(), "Missing content type");
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported content type: " + contentType);
        }

        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };

        Path dir = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String filename = userId + "_" + UUID.randomUUID() + ext;
        Path target = dir.resolve(filename);

        try (var in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/api/v1.0/users/avatars/" + filename;
    }
}
