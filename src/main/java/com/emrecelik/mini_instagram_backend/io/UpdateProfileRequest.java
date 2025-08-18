package com.emrecelik.mini_instagram_backend.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    private String bio;
}
