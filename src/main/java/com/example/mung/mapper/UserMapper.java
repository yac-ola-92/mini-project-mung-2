package com.example.mung.mapper;

import com.example.mung.domain.UserDTO;
import com.example.mung.domain.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {


    // User 전체 조회
    @Select("select * from user where user_id > 0 ")
    List<UserDTO> getList();

    // User 상세 조회
    @Select("select * from user where user_id = #{id}")
    UserDTO getOne(int id);


    // User 등록
    @Insert("INSERT INTO user (user_name,user_loginId, user_email, password, user_phone, user_birth, user_gender, nickname, role, user_address, profile_image_url, pet_info, business_number, business_sns_url) " +
            "VALUES (#{user_name},#{user_loginId}, #{user_email}, #{password}, #{user_phone}, #{user_birth}, #{user_gender}, #{nickname}, #{role}, #{user_address}, #{profile_image_url}, #{pet_info}, #{business_number}, #{business_sns_url})")
    boolean insert(UserVO vo);


    // User 수정
    @Update("UPDATE user SET user_loginId=#{user_loginId},user_name = #{user_name}, user_email = #{user_email}, password = #{password}, " +
            "user_phone = #{user_phone}, user_birth = #{user_birth}, user_gender = #{user_gender}, " +
            "nickname = #{nickname}, role = #{role}, user_address = #{user_address}, " +
            "profile_image_url = #{profile_image_url}, pet_info = #{pet_info} ,business_number=#{business_number},business_sns_url=#{business_sns_url} " +
            "WHERE user_id = #{user_id}")
    boolean update(UserVO vo);

    //한 컬럼만 변경 가능한지 체크한 메서드
    @Update("UPDATE user SET user_loginId=#{user_loginId}WHERE user_id = #{user_id}")
    boolean update1(UserVO vo);

    // User 삭제
    @Delete("delete from user where user_id = ${id}")
    boolean delete(int id);


}
