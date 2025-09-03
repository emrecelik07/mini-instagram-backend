package com.emrecelik.mini_instagram_backend.service;

import java.util.List;

import com.emrecelik.mini_instagram_backend.io.FollowRequest;
import com.emrecelik.mini_instagram_backend.io.FollowResponse;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;

public interface FollowService {
    
    FollowResponse followUser(String followerUserId, FollowRequest request);
    
    FollowResponse unfollowUser(String followerUserId, FollowRequest request);
    
    boolean isFollowing(String followerUserId, String followingUserId);
    
    List<ProfileResponse> getFollowing(String userId);
    
    List<ProfileResponse> getFollowers(String userId);
    
    Long getFollowingCount(String userId);
    
    Long getFollowersCount(String userId);
}

