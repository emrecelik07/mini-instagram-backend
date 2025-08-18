package com.emrecelik.mini_instagram_backend.service.impl;

import com.emrecelik.mini_instagram_backend.io.ProfileRequest;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.io.UpdateProfileRequest;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        UserModel newUser = convertUserToModel(profileRequest);

        if (!userRepository.existsByEmail(newUser.getEmail())) {
            newUser = userRepository.save(newUser);
            return createToProfileResponse(newUser);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + email));

        return createToProfileResponse(existingUser);

    }

    @Override
    public void sendResetOtp(String email) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + email));
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000;
        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);
        userRepository.save(existingUser);

        try {
            emailService.sendResetOtpEmail(email, otp);
        }catch (Exception e){
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + email));

        String resetOtp = existingUser.getResetOtp();
        if (!resetOtp.equals(otp) || resetOtp == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid OTP");
        }

        Long expireAt = existingUser.getResetOtpExpireAt();
        if (expireAt < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Expired OTP");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);

        userRepository.save(existingUser);
    }

    @Override
    public void sendOtp(String email) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + email));
        if(existingUser.getIsVerified()!=null && existingUser.getIsVerified()) {
            return;
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        long expiryTime = System.currentTimeMillis() + 24 * 60 *60* 1000;

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);

        userRepository.save(existingUser);

        try {
            emailService.sendOtpEmail(email, otp);
        }catch (Exception e){
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found" + email));
        if (!existingUser.getVerifyOtp().equals(otp) || existingUser.getVerifyOtpExpireAt() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid OTP");
        }
        if (existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Expired OTP");
        }

        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);
        existingUser.setIsVerified(true);

        userRepository.save(existingUser);
    }

    @Override
    public List<ProfileResponse> searchUsers(String searchTerm) {
        List<UserModel> users = userRepository.searchUsersByNameOrUsername(searchTerm);
        return users.stream()
                .map(this::createToProfileResponse)
                .toList();
    }

    @Override
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        UserModel existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Update only name and username if provided
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existingUser.setName(request.getName().trim());
        }
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            existingUser.setUsername(request.getUsername().trim());
        }
        if (request.getBio() != null) {
            existingUser.setBio(request.getBio().trim());
        }

        existingUser = userRepository.save(existingUser);
        return createToProfileResponse(existingUser);
    }

    private ProfileResponse createToProfileResponse(UserModel newUser) {
        return ProfileResponse.builder()
                .name(newUser.getName())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .isVerified(newUser.getIsVerified())
                .profileImageUrl(newUser.getProfileImageUrl())
                .bio(newUser.getBio())
                .build();
    }

    private UserModel convertUserToModel(ProfileRequest request) {
        return UserModel.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }


}
