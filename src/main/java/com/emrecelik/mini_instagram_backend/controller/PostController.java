package com.emrecelik.mini_instagram_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.emrecelik.mini_instagram_backend.io.PostRequest;
import com.emrecelik.mini_instagram_backend.io.PostResponse;
import com.emrecelik.mini_instagram_backend.io.CommentRequest;
import com.emrecelik.mini_instagram_backend.io.CommentResponse;
import com.emrecelik.mini_instagram_backend.service.PostService;
import com.emrecelik.mini_instagram_backend.util.IoUtil;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.getPost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String userId, Authentication authentication) {
        String currentUserId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        List<PostResponse> responses = postService.getPostsByUser(userId, currentUserId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/public")
    public ResponseEntity<List<PostResponse>> getAllPublicPosts(Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        List<PostResponse> responses = postService.getAllPublicPosts(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeedPosts(Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        List<PostResponse> responses = postService.getFeedPosts(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/liked")
    public ResponseEntity<List<PostResponse>> getLikedPosts(Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        List<PostResponse> responses = postService.getLikedPosts(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/saved")
    public ResponseEntity<List<PostResponse>> getSavedPosts(Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        List<PostResponse> responses = postService.getSavedPosts(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable String postId, 
                                                 @RequestBody PostRequest request, 
                                                 Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.updatePost(postId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        postService.deletePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.likePost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<PostResponse> unlikePost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.unlikePost(postId, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{postId}/save")
    public ResponseEntity<PostResponse> savePost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.savePost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/unsave")
    public ResponseEntity<PostResponse> unsavePost(@PathVariable String postId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        PostResponse response = postService.unsavePost(postId, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable String postId, 
                                                    @RequestBody CommentRequest request, 
                                                    Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        CommentResponse response = postService.addComment(postId, request, userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String postId) {
        List<CommentResponse> responses = postService.getComments(postId);
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) {
        String userId = IoUtil.getUserIdFromEmail(authentication.getName(), userRepository);
        postService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
