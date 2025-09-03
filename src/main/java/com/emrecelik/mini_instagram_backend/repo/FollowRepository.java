package com.emrecelik.mini_instagram_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrecelik.mini_instagram_backend.model.FollowModel;
import com.emrecelik.mini_instagram_backend.model.UserModel;

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {
    
    // Check if user A follows user B
    Optional<FollowModel> findByFollowerAndFollowing(UserModel follower, UserModel following);
    
    // Check if user A follows user B 
    @Query("SELECT f FROM FollowModel f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId")
    Optional<FollowModel> findByFollowerIdAndFollowingId(@Param("followerId") String followerId, @Param("followingId") String followingId);
    
    // Get all users that a user follows
    @Query("SELECT f.following FROM FollowModel f WHERE f.follower.userId = :userId")
    List<UserModel> findFollowingByUserId(@Param("userId") String userId);
    
    // Get all users that follow a user
    @Query("SELECT f.follower FROM FollowModel f WHERE f.following.userId = :userId")
    List<UserModel> findFollowersByUserId(@Param("userId") String userId);
    
    // Count how many users a user follows
    @Query("SELECT COUNT(f) FROM FollowModel f WHERE f.follower.userId = :userId")
    Long countFollowingByUserId(@Param("userId") String userId);
    
    // Count how many users follow a user
    @Query("SELECT COUNT(f) FROM FollowModel f WHERE f.following.userId = :userId")
    Long countFollowersByUserId(@Param("userId") String userId);
    
    // Check if user A follows user B
    boolean existsByFollowerUserIdAndFollowingUserId(String followerId, String followingId);
}

