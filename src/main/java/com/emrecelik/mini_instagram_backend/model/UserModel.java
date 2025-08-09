package com.emrecelik.mini_instagram_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userId;
    @Column(unique = true)
    private String username;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String bio;
    private String verifyOtp;
    private Boolean isVerified;
    private Long verifyOtpExpireAt;
    private String resetOtp;
    private Long resetOtpExpireAt;

    @Column(length = 2048)
    private String profileImageUrl;

    //metadata:
    private String profileImageContentType;
    private Integer profileImageWidth;
    private Integer profileImageHeight;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
