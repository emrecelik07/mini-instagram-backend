package com.emrecelik.mini_instagram_backend.service;

import com.emrecelik.mini_instagram_backend.io.ProfileRequest;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import org.springframework.context.annotation.Profile;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest profileRequest);
    ProfileResponse getProfile(String email);
    void sendResetOtp(String email);
    void resetPassword(String email, String otp, String newPassword);
    void  sendOtp(String email);
    void verifyOtp(String email, String otp);
}
