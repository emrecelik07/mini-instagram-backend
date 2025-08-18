package com.emrecelik.mini_instagram_backend.controller;

import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AvatarController {

    private final AvatarService avatarService;
    private final UserRepository userRepository;

    @Value("${app.uploads.dir}")
    private String uploadDir;

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProfileResponse uploadAvatar(@RequestParam("file") MultipartFile file, Authentication auth) throws Exception {
        String principal = auth.getName();

        UserModel user = userRepository.findByUsername(principal)
                .orElseGet(() -> userRepository.findByEmail(principal)
                        .orElseThrow(() -> new RuntimeException("User not found: " + principal)));

        String url = avatarService.saveAvatar(file, user.getId());
        user.setProfileImageUrl(url);
        userRepository.save(user);

        return ProfileResponse.builder()
                .userId(String.valueOf(user.getId()))
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .isVerified(user.getIsVerified())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .build();
    }

    @GetMapping("/avatars/{filename:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        try {

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(filename).normalize();

            if (!filePath.startsWith(uploadPath)) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {

                String contentType = determineContentType(filename);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (filename.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
