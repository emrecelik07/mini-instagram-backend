package com.emrecelik.mini_instagram_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.emrecelik.mini_instagram_backend.io.FollowRequest;
import com.emrecelik.mini_instagram_backend.io.FollowResponse;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.model.FollowModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.FollowRepository;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.FollowService;
import com.emrecelik.mini_instagram_backend.util.IoUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final IoUtil ioUtil;

    @Override
    public FollowResponse followUser(String followerUserId, FollowRequest request) {
        
        UserModel follower = userRepository.findByUserId(followerUserId)
                .orElseThrow(() -> new RuntimeException("Follower user not found"));
        
        UserModel following = userRepository.findByUserId(request.getFollowingUserId())
                .orElseThrow(() -> new RuntimeException("User to follow not found"));
        
       
        if (followRepository.existsByFollowerUserIdAndFollowingUserId(followerUserId, request.getFollowingUserId())) {
            throw new RuntimeException("Already following this user");
        }
        
        
        if (followerUserId.equals(request.getFollowingUserId())) {
            throw new RuntimeException("Cannot follow yourself");
        }
        
        
        FollowModel follow = FollowModel.builder()
                .follower(follower)
                .following(following)
                .build();
        
        followRepository.save(follow);
        
        return buildFollowResponse(follower, following, true);
    }

    @Override
    public FollowResponse unfollowUser(String followerUserId, FollowRequest request) {
       
        UserModel follower = userRepository.findByUserId(followerUserId)
                .orElseThrow(() -> new RuntimeException("Follower user not found"));
        
        UserModel following = userRepository.findByUserId(request.getFollowingUserId())
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));
        
       
        FollowModel follow = followRepository.findByFollowerIdAndFollowingId(followerUserId, request.getFollowingUserId())
                .orElseThrow(() -> new RuntimeException("Not following this user"));
        
       
        followRepository.delete(follow);
        
        return buildFollowResponse(follower, following, false);
    }

    @Override
    public boolean isFollowing(String followerUserId, String followingUserId) {
        return followRepository.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
    }

    @Override
    public List<ProfileResponse> getFollowing(String userId) {
        List<UserModel> following = followRepository.findFollowingByUserId(userId);
        return following.stream()
                .map(IoUtil::createToProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfileResponse> getFollowers(String userId) {
        List<UserModel> followers = followRepository.findFollowersByUserId(userId);
        return followers.stream()
                .map(IoUtil::createToProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getFollowingCount(String userId) {
        return followRepository.countFollowingByUserId(userId);
    }

    @Override
    public Long getFollowersCount(String userId) {
        return followRepository.countFollowersByUserId(userId);
    }
    
    private FollowResponse buildFollowResponse(UserModel follower, UserModel following, boolean isFollowing) {
        return FollowResponse.builder()
                .followerUserId(follower.getUserId())
                .followingUserId(following.getUserId())
                .followerUsername(follower.getUsername())
                .followingUsername(following.getUsername())
                .followerAvatar(follower.getProfileImageUrl())
                .followingAvatar(following.getProfileImageUrl())
                .isFollowing(isFollowing)
                .followersCount(getFollowersCount(following.getUserId()))
                .followingCount(getFollowingCount(following.getUserId()))
                .build();
    }
}
