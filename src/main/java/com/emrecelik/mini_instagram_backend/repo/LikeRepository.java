package com.emrecelik.mini_instagram_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrecelik.mini_instagram_backend.model.LikeModel;
import com.emrecelik.mini_instagram_backend.model.PostModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    
    Optional<LikeModel> findByUserAndPost(UserModel user, PostModel post);
    
    @Query("SELECT l FROM LikeModel l WHERE l.user.userId = :userId AND l.post.postId = :postId")
    Optional<LikeModel> findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);
    
    boolean existsByUserUserIdAndPostPostId(String userId, String postId);
    
    @Query("SELECT COUNT(l) FROM LikeModel l WHERE l.post.postId = :postId")
    Long countByPostId(@Param("postId") String postId);
    
    @Query("SELECT l FROM LikeModel l WHERE l.post.postId = :postId")
    List<LikeModel> findByPostId(@Param("postId") String postId);
    
    @Query("SELECT l FROM LikeModel l WHERE l.user.userId = :userId")
    List<LikeModel> findByUserId(@Param("userId") String userId);
    
    @Modifying
    @Query("DELETE FROM LikeModel l WHERE l.user.userId = :userId AND l.post.postId = :postId")
    void deleteByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);

    @Modifying
    @Query("DELETE FROM LikeModel l WHERE l.post.postId = :postId")
    void deleteAllByPostId(@Param("postId") String postId);
}
