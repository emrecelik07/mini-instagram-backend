package com.emrecelik.mini_instagram_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emrecelik.mini_instagram_backend.io.PostRequest;
import com.emrecelik.mini_instagram_backend.io.PostResponse;
import com.emrecelik.mini_instagram_backend.io.CommentRequest;
import com.emrecelik.mini_instagram_backend.io.CommentResponse;
import com.emrecelik.mini_instagram_backend.model.PostModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;
import com.emrecelik.mini_instagram_backend.model.LikeModel;
import com.emrecelik.mini_instagram_backend.model.SaveModel;
import com.emrecelik.mini_instagram_backend.model.CommentModel;
import com.emrecelik.mini_instagram_backend.repo.UserRepository;
import com.emrecelik.mini_instagram_backend.repo.PostRepository;
import com.emrecelik.mini_instagram_backend.repo.LikeRepository;
import com.emrecelik.mini_instagram_backend.repo.SaveRepository;
import com.emrecelik.mini_instagram_backend.repo.CommentRepository;
import com.emrecelik.mini_instagram_backend.service.PostService;
import com.emrecelik.mini_instagram_backend.util.IoUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final SaveRepository saveRepository;
    private final CommentRepository commentRepository;
    private final IoUtil ioUtil;
    
    @Override
    public PostResponse createPost(PostRequest request, String userId) {
        UserModel user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        PostModel post = ioUtil.convertPostRequestToModel(request, user);
        PostModel savedPost = postRepository.save(post);
        
        return IoUtil.convertPostToResponse(savedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Override
    public PostResponse getPost(String postId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return IoUtil.convertPostToResponse(post);
    }
    
    public PostResponse getPost(String postId, String currentUserId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return IoUtil.convertPostToResponse(post, currentUserId, likeRepository, saveRepository, commentRepository);
    }
    
    @Override
    public List<PostResponse> getPostsByUser(String userId) {
        List<PostModel> posts = postRepository.findByUser_UserId(userId);
        return posts.stream()
                .map(IoUtil::convertPostToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getPostsByUser(String userId, String currentUserId) {
        List<PostModel> posts = postRepository.findByUser_UserId(userId);
        return posts.stream()
                .map(post -> IoUtil.convertPostToResponse(post, currentUserId, likeRepository, saveRepository, commentRepository))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PostResponse> getAllPublicPosts() {
        List<PostModel> posts = postRepository.findAllPublicPosts();
        return posts.stream()
                .map(IoUtil::convertPostToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getAllPublicPosts(String currentUserId) {
        List<PostModel> posts = postRepository.findAllPublicPosts();
        return posts.stream()
                .map(post -> IoUtil.convertPostToResponse(post, currentUserId, likeRepository, saveRepository, commentRepository))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PostResponse> getFeedPosts(String userId) {
        List<PostModel> posts = postRepository.getFeedPosts(userId);
        return posts.stream()
                .map(post -> IoUtil.convertPostToResponse(post, userId, likeRepository, saveRepository, commentRepository))
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getLikedPosts(String userId) {
        List<PostModel> posts = likeRepository.findByUserId(userId)
                .stream()
                .map(LikeModel::getPost)
                .collect(Collectors.toList());
        return posts.stream()
                .map(post -> IoUtil.convertPostToResponse(post, userId, likeRepository, saveRepository, commentRepository))
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getSavedPosts(String userId) {
        List<PostModel> posts = saveRepository.findSavedPostsByUserId(userId);
        return posts.stream()
                .map(post -> IoUtil.convertPostToResponse(post, userId, likeRepository, saveRepository, commentRepository))
                .collect(Collectors.toList());
    }
    
    @Override
    public PostResponse updatePost(String postId, PostRequest request, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this post");
        }
        
        if (request.getCaption() != null) {
            post.setCaption(request.getCaption());
        }
        if (request.getImageUrl() != null) {
            post.setImageUrl(request.getImageUrl());
        }
        if (request.getVideoUrl() != null) {
            post.setVideoUrl(request.getVideoUrl());
        }
        if (request.getIsPrivate() != null) {
            post.setIsPrivate(request.getIsPrivate());
        }
        if (request.getLocation() != null) {
            post.setLocation(request.getLocation());
        }
        if (request.getTags() != null) {
            post.setTags(request.getTags());
        }
        
        PostModel updatedPost = postRepository.save(post);
        return IoUtil.convertPostToResponse(updatedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Override
    @Transactional
    public void deletePost(String postId, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }
        // Delete child records first to satisfy FK constraints
        likeRepository.deleteAllByPostId(postId);
        saveRepository.deleteAllByPostId(postId);
        commentRepository.deleteAllByPostId(postId);

        postRepository.delete(post);
    }
    
    @Override
    @Transactional
    public PostResponse likePost(String postId, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        UserModel user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (likeRepository.existsByUserUserIdAndPostPostId(userId, postId)) {
            throw new RuntimeException("User has already liked this post");
        }
        
        LikeModel like = LikeModel.builder()
                .user(user)
                .post(post)
                .build();
        
        likeRepository.save(like);
        
        post.setLikeCount(post.getLikeCount() + 1);
        PostModel updatedPost = postRepository.save(post);
        
        return IoUtil.convertPostToResponse(updatedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Override
    @Transactional
    public PostResponse unlikePost(String postId, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!likeRepository.existsByUserUserIdAndPostPostId(userId, postId)) {
            throw new RuntimeException("User has not liked this post");
        }
        
        likeRepository.deleteByUserIdAndPostId(userId, postId);
        
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
        }
        PostModel updatedPost = postRepository.save(post);
        
        return IoUtil.convertPostToResponse(updatedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Transactional
    public PostResponse savePost(String postId, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        UserModel user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (saveRepository.existsByUserUserIdAndPostPostId(userId, postId)) {
            throw new RuntimeException("User has already saved this post");
        }
        
        SaveModel save = SaveModel.builder()
                .user(user)
                .post(post)
                .build();
        
        saveRepository.save(save);
        
        post.setSaveCount(post.getSaveCount() + 1);
        PostModel updatedPost = postRepository.save(post);
        
        return IoUtil.convertPostToResponse(updatedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Transactional
    public PostResponse unsavePost(String postId, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!saveRepository.existsByUserUserIdAndPostPostId(userId, postId)) {
            throw new RuntimeException("User has not saved this post");
        }
        
        saveRepository.deleteByUserIdAndPostId(userId, postId);
        
        if (post.getSaveCount() > 0) {
            post.setSaveCount(post.getSaveCount() - 1);
        }
        PostModel updatedPost = postRepository.save(post);
        
        return IoUtil.convertPostToResponse(updatedPost, userId, likeRepository, saveRepository, commentRepository);
    }
    
    @Transactional
    public CommentResponse addComment(String postId, CommentRequest request, String userId) {
        PostModel post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        UserModel user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CommentModel comment = ioUtil.convertCommentRequestToModel(request, user, post);
        CommentModel savedComment = commentRepository.save(comment);
        
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
        
        return IoUtil.convertCommentToResponse(savedComment);
    }
    
    @Transactional
    public void deleteComment(String commentId, String userId) {
        CommentModel comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this comment");
        }
        
        PostModel post = comment.getPost();
        commentRepository.delete(comment);
        
        if (post.getCommentCount() > 0) {
            post.setCommentCount(post.getCommentCount() - 1);
            postRepository.save(post);
        }
    }
    
    public List<CommentResponse> getComments(String postId) {
        List<CommentModel> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(IoUtil::convertCommentToResponse)
                .collect(Collectors.toList());
    }
}
