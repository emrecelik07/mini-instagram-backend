package com.emrecelik.mini_instagram_backend.repo;

import com.emrecelik.mini_instagram_backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<UserModel> findByUsername(String principal);

    @Query("SELECT u FROM UserModel u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<UserModel> searchUsersByNameOrUsername(@Param("searchTerm") String searchTerm);
}
