package com.emrecelik.mini_instagram_backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowResponse {
    private String followerUserId;
    private String followingUserId;
    private String followerUsername;
    private String followingUsername;
    private String followerAvatar;
    private String followingAvatar;
    private boolean isFollowing;
    private Long followersCount;
    private Long followingCount;
}

