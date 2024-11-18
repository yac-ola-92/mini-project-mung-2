package com.example.mung.repository;

import com.example.mung.entity.UserEntity;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginRepository extends JpaRepository<UserEntity,Integer> {

    //아이디 전체 출력
    @Query(value = "SELECT user_login_id FROM user", nativeQuery = true)
    List<String> idList();

    //아이디 비밀번호 정보조회 -> 로그인시 정보
    @Query(value = "SELECT * FROM user WHERE user_login_id = :id ", nativeQuery = true)
    UserEntity getUserByLoginIdAndPassword(@Param("id") String id);

    //User 아이디 중복 조회할 때 사용할 메서드
    @Query(value = "select count(*)>0 from user where user_login_id =:id",nativeQuery = true)
   Long checkId(@Param("id") String id);

    //아이디로 존재여부 확인 -> 이름출력
    @Query(value = "select user_name from user where user_login_id = :id" ,nativeQuery = true)
    String getUserNameByLoginId(@Param("id") String id);
    //일반 회원 가입 save()로 처리

    //이름, 이메일, 생년월일 아이디 찾기
    @Query(value = "select user_login_id from user where user_name =:name and user_email=:email and user_birth=:birth",nativeQuery = true)
    String checkIdByNameEmailBirth(@Param("name") String name,@Param("email")String email,@Param("birth") LocalDateTime birth);

    //비밀 번호 수정 전 아이디, 이메일, 생년월일 확인
    @Query(value = "select user_name from user where user_login_id =:id and user_email=:email and user_birth=:birth",nativeQuery = true)
    String checkIdByIdEmailBirth(@Param("id") String id,@Param("email")String email,@Param("birth") LocalDateTime birth);

    //비밀번호 수정
    @Modifying
    @Query(value = "UPDATE user SET password=:password WHERE user_login_id = :id and user_phone = :phone",nativeQuery = true)
    int updatePasswordByLoginId(String id, String password,String phone);

    //전화번호로 아이디 찾기
    @Query(value = "select user_login_id from user where user_phone = :phone ",nativeQuery = true)
    String checkPhoneByLoginId(@Param("phone") String phone);
}
