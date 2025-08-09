package com.emrecelik.mini_instagram_backend.repo;

import com.emrecelik.mini_instagram_backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<UserModel> findByUsername(String principal);
}
