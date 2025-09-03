package com.emrecelik.mini_instagram_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.emrecelik.mini_instagram_backend.io.FollowRequest;
import com.emrecelik.mini_instagram_backend.io.FollowResponse;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.FollowService;
import com.emrecelik.mini_instagram_backend.util.IoUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowService followService;
    private final UserRepository userRepository;

    @PostMapping("/follow")
    public ResponseEntity<FollowResponse> followUser(@RequestBody FollowRequest request, Authentication authentication) {
        String followerUserId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        FollowResponse response = followService.followUser(followerUserId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<FollowResponse> unfollowUser(@RequestBody FollowRequest request, Authentication authentication) {
        String followerUserId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        FollowResponse response = followService.unfollowUser(followerUserId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/{followingUserId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String followingUserId, Authentication authentication) {
        String followerUserId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        boolean isFollowing = followService.isFollowing(followerUserId, followingUserId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<ProfileResponse>> getFollowing(@PathVariable String userId) {
        List<ProfileResponse> responses = followService.getFollowing(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<ProfileResponse>> getFollowers(@PathVariable String userId) {
        List<ProfileResponse> responses = followService.getFollowers(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/counts/{userId}")
    public ResponseEntity<FollowResponse> getFollowCounts(@PathVariable String userId) {
        Long followingCount = followService.getFollowingCount(userId);
        Long followersCount = followService.getFollowersCount(userId);
        
        FollowResponse response = FollowResponse.builder()
                .followingUserId(userId)
                .followingCount(followingCount)
                .followersCount(followersCount)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

