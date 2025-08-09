package com.emrecelik.mini_instagram_backend.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotNull
    @Email
    private String email;
    @Size(min = 6)
    private String password;

    private String profileImageUrl;

}
