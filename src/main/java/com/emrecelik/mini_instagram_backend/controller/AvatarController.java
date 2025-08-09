package com.emrecelik.mini_instagram_backend.controller;

import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AvatarController {

    private final AvatarService avatarService;
    private final UserRepository userRepository;

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
                .build();
    }
}
