package com.emrecelik.mini_instagram_backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private String userId;
    private String name;
    private String username;
    private String email;
    private Boolean isVerified;
    private String profileImageUrl;
    private String bio;
    private Long followersCount;
    private Long followingCount;
}
