package com.emrecelik.mini_instagram_backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String postId;
    private String caption;
    private String imageUrl;
    private String videoUrl;
    private Boolean isPrivate;
    private String location;
    private String tags;
    
    private String userId;
    private String username;
    private String userAvatar;
    
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private Integer viewCount;
    private Integer saveCount;
    private Integer repostCount;
    
    private Boolean isLikedByCurrentUser;
    private Boolean isSavedByCurrentUser;
    
    private List<CommentResponse> comments;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
