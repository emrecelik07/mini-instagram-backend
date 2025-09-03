package com.emrecelik.mini_instagram_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emrecelik.mini_instagram_backend.io.FollowRequest;
import com.emrecelik.mini_instagram_backend.io.FollowResponse;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.model.FollowModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.repo.FollowRepository;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.service.impl.FollowServiceImpl;
import com.emrecelik.mini_instagram_backend.util.IoUtil;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IoUtil ioUtil;

    @InjectMocks
    private FollowServiceImpl followService;

    private UserModel follower;
    private UserModel following;
    private FollowRequest followRequest;

    @BeforeEach
    void setUp() {
        follower = UserModel.builder()
                .userId("user1")
                .username("john123")
                .name("John Doe")
                .profileImageUrl("https://example.com/john.jpg")
                .build();

        following = UserModel.builder()
                .userId("user2")
                .username("jane456")
                .name("Jane Smith")
                .profileImageUrl("https://example.com/jane.jpg")
                .build();

        followRequest = FollowRequest.builder()
                .followingUserId("user2")
                .build();
    }

    @Test
    void followUser_Success() {
        // Arrange
        when(userRepository.findByUserId("user1")).thenReturn(Optional.of(follower));
        when(userRepository.findByUserId("user2")).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerUserIdAndFollowingUserId("user1", "user2")).thenReturn(false);
        when(followRepository.save(any(FollowModel.class))).thenReturn(new FollowModel());
        when(followRepository.countFollowersByUserId("user2")).thenReturn(1L);
        when(followRepository.countFollowingByUserId("user2")).thenReturn(0L);

        // Act
        FollowResponse response = followService.followUser("user1", followRequest);

        // Assert
        assertNotNull(response);
        assertEquals("user1", response.getFollowerUserId());
        assertEquals("user2", response.getFollowingUserId());
        assertTrue(response.isFollowing());
        verify(followRepository).save(any(FollowModel.class));
    }

    @Test
    void followUser_AlreadyFollowing_ThrowsException() {
        // Arrange
        when(userRepository.findByUserId("user1")).thenReturn(Optional.of(follower));
        when(userRepository.findByUserId("user2")).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerUserIdAndFollowingUserId("user1", "user2")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            followService.followUser("user1", followRequest);
        });
        verify(followRepository, never()).save(any());
    }

    @Test
    void followUser_SelfFollow_ThrowsException() {
        // Arrange
        followRequest.setFollowingUserId("user1");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            followService.followUser("user1", followRequest);
        });
        verify(followRepository, never()).save(any());
    }

    @Test
    void unfollowUser_Success() {
        // Arrange
        FollowModel existingFollow = new FollowModel();
        when(userRepository.findByUserId("user1")).thenReturn(Optional.of(follower));
        when(userRepository.findByUserId("user2")).thenReturn(Optional.of(following));
        when(followRepository.findByFollowerIdAndFollowingId("user1", "user2")).thenReturn(Optional.of(existingFollow));
        when(followRepository.countFollowersByUserId("user2")).thenReturn(0L);
        when(followRepository.countFollowingByUserId("user2")).thenReturn(0L);

        // Act
        FollowResponse response = followService.unfollowUser("user1", followRequest);

        // Assert
        assertNotNull(response);
        assertFalse(response.isFollowing());
        verify(followRepository).delete(existingFollow);
    }

    @Test
    void getFollowing_Success() {
        // Arrange
        List<UserModel> followingUsers = Arrays.asList(following);
        when(followRepository.findFollowingByUserId("user1")).thenReturn(followingUsers);
        when(ioUtil.createToProfileResponse(following)).thenReturn(new ProfileResponse());

        // Act
        List<ProfileResponse> response = followService.getFollowing("user1");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void getFollowers_Success() {
        // Arrange
        List<UserModel> followers = Arrays.asList(follower);
        when(followRepository.findFollowersByUserId("user2")).thenReturn(followers);
        when(ioUtil.createToProfileResponse(follower)).thenReturn(new ProfileResponse());

        // Act
        List<ProfileResponse> response = followService.getFollowers("user2");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void getFollowCounts_Success() {
        // Arrange
        when(followRepository.countFollowingByUserId("user1")).thenReturn(2L);
        when(followRepository.countFollowersByUserId("user1")).thenReturn(1L);

        // Act
        Long followingCount = followService.getFollowingCount("user1");
        Long followersCount = followService.getFollowersCount("user1");

        // Assert
        assertEquals(2L, followingCount);
        assertEquals(1L, followersCount);
    }
}

