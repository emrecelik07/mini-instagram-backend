package com.emrecelik.mini_instagram_backend.controller;

import com.emrecelik.mini_instagram_backend.io.AuthRequest;
import com.emrecelik.mini_instagram_backend.io.AuthResponse;
import com.emrecelik.mini_instagram_backend.io.ResetPasswordRequest;
import com.emrecelik.mini_instagram_backend.service.ProfileService;
import com.emrecelik.mini_instagram_backend.service.impl.AppUserDetailsService;
import com.emrecelik.mini_instagram_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {
            boolean secure = false;
            String sameSite = "Lax";

            authenticate(authRequest.getEmail(), authRequest.getPassword());
            final UserDetails userDetails = appUserDetailsService
                    .loadUserByUsername(authRequest.getEmail());

            boolean remember = Boolean.TRUE.equals(authRequest.getRememberMe());
            Duration ttl     = remember ? Duration.ofDays(7) : Duration.ofHours(1);
            String jwttoken  = jwtUtil.generateToken(userDetails, ttl);

            ResponseCookie responseCookie = ResponseCookie.from("jwt", jwttoken)
                    .httpOnly(true)
                    .path("/")
                    .secure(secure)
                    .sameSite(sameSite)
                    .maxAge(remember ? Duration.ofDays(7) : Duration.ofHours(1))
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(Map.of("email", authRequest.getEmail()));

        } catch (BadCredentialsException e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", true);
            errors.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

        } catch (DisabledException e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", true);
            errors.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);

        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", true);
            errors.put("message", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);

        }
    }

    private void authenticate(String email, String password) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {

        return ResponseEntity.ok(email != null);
    }

    @PostMapping(path = "/send-reset-otp")
    public void sendResetOtp(@RequestParam String email) {

        try {
            profileService.sendResetOtp(email);
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        try {

            profileService.resetPassword(
                    resetPasswordRequest.getEmail(),
                    resetPasswordRequest.getOtp(),
                    resetPasswordRequest.getNewPassword());

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(path = "/send-otp")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication.name") String email) {
        try {
            profileService.sendOtp(email);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public void verifyOtp(@RequestBody Map<String, Object> request,
                          @CurrentSecurityContext(expression = "authentication?.name") String email) {

        if (request.get("otp") == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP required");
        }

        try {
            profileService.verifyOtp(email, request.get("otp").toString());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully!");

    }
}
