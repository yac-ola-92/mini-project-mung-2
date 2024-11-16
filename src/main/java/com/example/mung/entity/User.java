package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, unique = true, length = 100)
    private String userEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String userPhone;

    private LocalDateTime userBirth;

    @Column(nullable = false)
    private int userGender; // 1,3: 남자 / 2,4: 여성

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false)
    private String role; // USER, HOST, ADMIN

    private String profileImageUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String petInfo; // JSON 형태로 저장

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private String businessNumber;

    private String businessSnsUrl;

    @Column(name = "user_login_id", nullable = false, unique = true, length = 50)
    private String userLoginId;

    // JSON 형태로 저장된 반려동물 정보 변환 메서드
    public String getPetInfoToString() {
        if (this.petInfo == null) {
            return "반려동물 정보가 없습니다.";
        }

        JSONObject jo = new JSONObject(petInfo);
        String name = jo.optString("이름", "정보 없음");
        String type = jo.optString("종", "정보 없음");
        String age = jo.optString("나이", "정보 없음");
        String weight = jo.optString("무게", "정보 없음");

        return String.format("이름: %s, 종: %s, 나이: %s, 무게: %s", name, type, age, weight);
    }
}
