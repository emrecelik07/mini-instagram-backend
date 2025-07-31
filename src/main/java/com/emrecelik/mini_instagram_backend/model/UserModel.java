package com.emrecelik.mini_instagram_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.security.Timestamp;
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
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String verifyOtp;
    private Boolean isVerified;
    private Long verifyOtpExpireAt;
    private String resetOtp;
    private Long resetOtpExpireAt;

//    @CreationTimestamp
//    @Column(updatable = false)
//    private Timestamp createdAt;
//    @UpdateTimestamp
//    private Timestamp updatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
