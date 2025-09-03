package com.emrecelik.mini_instagram_backend.service;

import java.util.List;

import com.emrecelik.mini_instagram_backend.io.PostRequest;
import com.emrecelik.mini_instagram_backend.io.PostResponse;
import com.emrecelik.mini_instagram_backend.io.CommentRequest;
import com.emrecelik.mini_instagram_backend.io.CommentResponse;
import com.emrecelik.mini_instagram_backend.model.PostModel;

public interface PostService {
    
    PostResponse createPost(PostRequest request, String userId);
    
    PostResponse getPost(String postId);
    
    PostResponse getPost(String postId, String currentUserId);
    
    List<PostResponse> getPostsByUser(String userId);
    
    List<PostResponse> getPostsByUser(String userId, String currentUserId);
    
    List<PostResponse> getAllPublicPosts();
    
    List<PostResponse> getAllPublicPosts(String currentUserId);
    
    List<PostResponse> getFeedPosts(String userId);
    
    List<PostResponse> getLikedPosts(String userId);
    
    List<PostResponse> getSavedPosts(String userId);
    
    PostResponse updatePost(String postId, PostRequest request, String userId);
    
    void deletePost(String postId, String userId);
    
    PostResponse likePost(String postId, String userId);
    
    PostResponse unlikePost(String postId, String userId);
    
    PostResponse savePost(String postId, String userId);
    
    PostResponse unsavePost(String postId, String userId);
    
    CommentResponse addComment(String postId, CommentRequest request, String userId);
    
    void deleteComment(String commentId, String userId);
    
    List<CommentResponse> getComments(String postId);
}
