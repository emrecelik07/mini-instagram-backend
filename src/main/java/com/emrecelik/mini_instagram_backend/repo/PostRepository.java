package com.emrecelik.mini_instagram_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrecelik.mini_instagram_backend.model.PostModel;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    
    Optional<PostModel> findByPostId(String postId);
    
    List<PostModel> findByUser_UserId(String userId);
    
    @Query("SELECT p FROM PostModel p WHERE p.isPrivate = false ORDER BY p.createdAt DESC")
    List<PostModel> findAllPublicPosts();
    
    List<PostModel> findByUser_UserIdAndIsPrivateOrderByCreatedAtDesc(String userId, Boolean isPrivate);
    
    boolean existsByPostId(String postId);
    
    // Get feed posts from users that the current user follows
    @Query("SELECT p FROM PostModel p WHERE p.user IN " +
            "(SELECT f.following FROM FollowModel f WHERE f.follower.userId = :userId) " +
            "AND p.isPrivate = false ORDER BY p.createdAt DESC")
    List<PostModel> getFeedPosts(@Param("userId") String userId);
}
