package com.emrecelik.mini_instagram_backend.controller;

import com.emrecelik.mini_instagram_backend.io.ProfileRequest;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.io.UpdateProfileRequest;
import com.emrecelik.mini_instagram_backend.service.impl.EmailService;
import com.emrecelik.mini_instagram_backend.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        emailService.sendWelcomeEmail(response.getEmail(), response.getName());
        return response;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        return profileService.getProfile(email);
    }

    @GetMapping("/search")
    public List<ProfileResponse> searchUsers(@RequestParam String q) {
        return profileService.searchUsers(q);
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(
            @CurrentSecurityContext(expression = "authentication?.name") String email,
            @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(email, request);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        profileService.deleteCurrentUser(email);
    }

}
