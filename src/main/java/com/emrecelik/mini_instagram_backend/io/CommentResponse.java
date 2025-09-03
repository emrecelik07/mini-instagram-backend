package com.emrecelik.mini_instagram_backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String commentId;
    private String content;
    private String userId;
    private String username;
    private String userAvatar;
    private String postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

