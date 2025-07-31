package com.emrecelik.mini_instagram_backend.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank
    private String newPassword;
    @NotBlank
    private String otp;
    @NotBlank
    private String email;
}
