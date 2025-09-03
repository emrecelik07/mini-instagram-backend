package com.emrecelik.mini_instagram_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrecelik.mini_instagram_backend.model.CommentModel;
import com.emrecelik.mini_instagram_backend.model.PostModel;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    
    Optional<CommentModel> findByCommentId(String commentId);
    
    @Query("SELECT c FROM CommentModel c WHERE c.post.postId = :postId ORDER BY c.createdAt DESC")
    List<CommentModel> findByPostId(@Param("postId") String postId);
    
    @Query("SELECT COUNT(c) FROM CommentModel c WHERE c.post.postId = :postId")
    Long countByPostId(@Param("postId") String postId);
    
    @Query("SELECT c FROM CommentModel c WHERE c.user.userId = :userId ORDER BY c.createdAt DESC")
    List<CommentModel> findByUserId(@Param("userId") String userId);
    
    boolean existsByCommentId(String commentId);
}

