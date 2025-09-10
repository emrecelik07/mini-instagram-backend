package com.emrecelik.mini_instagram_backend.util;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.emrecelik.mini_instagram_backend.io.ProfileRequest;
import com.emrecelik.mini_instagram_backend.io.ProfileResponse;
import com.emrecelik.mini_instagram_backend.io.PostRequest;
import com.emrecelik.mini_instagram_backend.io.PostResponse;
import com.emrecelik.mini_instagram_backend.io.CommentRequest;
import com.emrecelik.mini_instagram_backend.io.CommentResponse;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.model.PostModel;
import com.emrecelik.mini_instagram_backend.model.CommentModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.repo.LikeRepository;
import com.emrecelik.mini_instagram_backend.repo.SaveRepository;
import com.emrecelik.mini_instagram_backend.repo.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IoUtil {

    private final PasswordEncoder passwordEncoder;

    public static ProfileResponse createToProfileResponse(UserModel newUser) {
        return ProfileResponse.builder()
                .name(newUser.getName())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .userId(newUser.getUserId())
                .isVerified(newUser.getIsVerified())
                .profileImageUrl(newUser.getProfileImageUrl())
                .bio(newUser.getBio())
                .build();
    }

    public static ProfileResponse attachFollowCounts(ProfileResponse base, Long followersCount, Long followingCount) {
        base.setFollowersCount(followersCount);
        base.setFollowingCount(followingCount);
        return base;
    }

    public UserModel convertUserToModel(ProfileRequest request) {
        
        return UserModel.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }

    public static PostResponse convertPostToResponse(PostModel post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .caption(post.getCaption())
                .imageUrl(post.getImageUrl())
                .videoUrl(post.getVideoUrl())
                .isPrivate(post.getIsPrivate())
                .location(post.getLocation())
                .tags(post.getTags())
                .userId(post.getUser().getUserId())
                .username(post.getUser().getUsername())
                .userAvatar(post.getUser().getProfileImageUrl())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .shareCount(post.getShareCount())
                .viewCount(post.getViewCount())
                .saveCount(post.getSaveCount())
                .repostCount(post.getRepostCount())
                .isLikedByCurrentUser(false)
                .isSavedByCurrentUser(false)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponse convertPostToResponse(PostModel post, String currentUserId, 
                                                   LikeRepository likeRepository, 
                                                   SaveRepository saveRepository,
                                                   CommentRepository commentRepository) {
        boolean isLikedByCurrentUser = likeRepository.existsByUserUserIdAndPostPostId(currentUserId, post.getPostId());
        boolean isSavedByCurrentUser = saveRepository.existsByUserUserIdAndPostPostId(currentUserId, post.getPostId());
        
        List<CommentResponse> comments = commentRepository.findByPostId(post.getPostId())
                .stream()
                .map(IoUtil::convertCommentToResponse)
                .collect(Collectors.toList());
        
        return PostResponse.builder()
                .postId(post.getPostId())
                .caption(post.getCaption())
                .imageUrl(post.getImageUrl())
                .videoUrl(post.getVideoUrl())
                .isPrivate(post.getIsPrivate())
                .location(post.getLocation())
                .tags(post.getTags())
                .userId(post.getUser().getUserId())
                .username(post.getUser().getUsername())
                .userAvatar(post.getUser().getProfileImageUrl())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .shareCount(post.getShareCount())
                .viewCount(post.getViewCount())
                .saveCount(post.getSaveCount())
                .repostCount(post.getRepostCount())
                .isLikedByCurrentUser(isLikedByCurrentUser)
                .isSavedByCurrentUser(isSavedByCurrentUser)
                .comments(comments)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static CommentResponse convertCommentToResponse(CommentModel comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .userAvatar(comment.getUser().getProfileImageUrl())
                .postId(comment.getPost().getPostId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public CommentModel convertCommentRequestToModel(CommentRequest request, UserModel user, PostModel post) {
        return CommentModel.builder()
                .commentId(UUID.randomUUID().toString())
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();
    }

    public PostModel convertPostRequestToModel(PostRequest request, UserModel user) {
        return PostModel.builder()
                .postId(UUID.randomUUID().toString())
                .user(user)
                .caption(request.getCaption())
                .imageUrl(request.getImageUrl())
                .videoUrl(request.getVideoUrl())
                .isPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : false)
                .location(request.getLocation())
                .tags(request.getTags())
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .viewCount(0)
                .saveCount(0)
                .repostCount(0)
                .build();
    }

    public static boolean isValidImageFile(String filename) {
        return filename != null && (filename.endsWith(".jpg") || 
               filename.endsWith(".png") || filename.endsWith(".webp"));
    }

    public static String getUserIdFromEmail(String email, UserRepository userRepository) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }
    
}
