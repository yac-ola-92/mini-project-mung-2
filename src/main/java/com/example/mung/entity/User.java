package com.example.mung.entity;

import com.example.mung.domain.UserVO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @Column(name = "user_login_id", nullable = false)
    private String user_login_id;
    @Column(name = "user_name", nullable = false)
    private String user_name;
    @Column(nullable = false)
    private String password;
    private String nickname;
    @Column(name = "user_email", nullable = false)
    private String user_email;
    @Column(name = "user_phone")
    private String user_phone;
    @Column(name = "user_birth")
    private LocalDateTime user_birth;
    @Column(name = "user_gender")
    private int user_gender;
    @Column(name = "profile_image_url")
    private String profile_image_url;
    private String role;
    @Column(name = "pet_info", nullable = false)
    private String pet_info;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;
    @Column(name = "business_number")
    private String business_number;
    @Column(name = "business_sns_url")
    private String business_sns_url;

    public User() {

    }

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = LocalDateTime.now();
    }


    //엔티티 때문에 만든 변환 생성자
    public User(UserVO user) {
        this.user_id = user.getUser_id();
        this.user_name = user.getUser_name();
        this.user_email = user.getUser_email();
        this.password = user.getPassword();
        this.user_phone = user.getUser_phone();
        this.user_birth = user.getUser_birth();
        this.user_gender = user.getUser_gender();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.business_number = user.getBusiness_number();
        this.business_sns_url = user.getBusiness_sns_url();
        this.profile_image_url = user.getProfile_image_url();
        this.pet_info = user.getPet_info();
        this.user_login_id = user.getUser_loginId();
    }

}




