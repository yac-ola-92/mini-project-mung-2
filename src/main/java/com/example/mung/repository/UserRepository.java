package com.example.mung.repository;

import com.example.mung.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // User 전체 조회
    List<UserEntity> findAll();
    // User 상세 조회
    UserEntity findById(int id);
    // User 등록 save()사용
    @Query(value = "select * from user where user_login_id = :id",nativeQuery = true)
    Optional<UserEntity> findByUserLoginId(String id);
    // User 수정 save() 사용

    // User 강아지 정보 수정



}
