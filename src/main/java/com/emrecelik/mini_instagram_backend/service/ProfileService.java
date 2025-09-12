package com.emrecelik.mini_instagram_backend.service;

import com.emrecelik.mini_instagram_backend.io.ProfileRequest;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.io.UpdateProfileRequest;

import java.util.List;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest profileRequest);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String otp, String newPassword);
    void changePassword(String email, String currentPassword, String newPassword);
    void  sendOtp(String email);
    void verifyOtp(String email, String otp);
    List<ProfileResponse> searchUsers(String searchTerm);
    ProfileResponse updateProfile(String email, UpdateProfileRequest request);
    void deleteCurrentUser(String email);
}
