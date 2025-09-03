package com.emrecelik.mini_instagram_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrecelik.mini_instagram_backend.model.SaveModel;
import com.emrecelik.mini_instagram_backend.model.PostModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;

@Repository
public interface SaveRepository extends JpaRepository<SaveModel, Long> {
    
    Optional<SaveModel> findByUserAndPost(UserModel user, PostModel post);
    
    @Query("SELECT s FROM SaveModel s WHERE s.user.userId = :userId AND s.post.postId = :postId")
    Optional<SaveModel> findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);
    
    boolean existsByUserUserIdAndPostPostId(String userId, String postId);
    
    @Query("SELECT COUNT(s) FROM SaveModel s WHERE s.post.postId = :postId")
    Long countByPostId(@Param("postId") String postId);
    
    @Query("SELECT s.post FROM SaveModel s WHERE s.user.userId = :userId ORDER BY s.createdAt DESC")
    List<PostModel> findSavedPostsByUserId(@Param("userId") String userId);
    
    @Modifying
    @Query("DELETE FROM SaveModel s WHERE s.user.userId = :userId AND s.post.postId = :postId")
    void deleteByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);
}

