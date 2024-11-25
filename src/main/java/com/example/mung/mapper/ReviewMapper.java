package com.example.mung.mapper;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {
    @Select("SELECT r.review_id, u.nickname, r.rv_id, r.rating, r.comment, r.created_at, " +
            "rm.room_name, a.accom_name, a.accom_images_url " +
            "FROM review r " +
            "JOIN user u ON r.user_id = u.user_id " +
            "JOIN reservation res ON r.rv_id = res.rv_id " +
            "JOIN room rm ON res.room_id = rm.room_id " +
            "JOIN accommodation a ON res.accom_id = a.accom_id")
    List<ReviewVO> getAllReviews();

    @Select("SELECT r.review_id, r.rv_id, r.rating, r.comment, r.created_at, r.user_id\n" +
            "FROM review r\n" +
            "JOIN reservation res ON r.rv_id = res.rv_id\n" +
            "WHERE r.user_id =#{user_id}")
    List<ReviewVO> getReviewsByUserId(@Param("user_id") int user_id);

    @Select("SELECT r.review_id, r.user_id, r.rv_id, r.rating, r.comment, r.created_at, " +
            "rm.room_name, a.accom_name " +
            "FROM review r " +
            "JOIN reservation res ON r.rv_id = res.rv_id " +
            "JOIN room rm ON res.room_id = rm.room_id " +
            "JOIN accommodation a ON res.accom_id = a.accom_id " +
            "WHERE r.review_id = #{review_id}")
    ReviewVO getReviewById(@Param("review_id") int review_id);

    @Insert("INSERT INTO review (user_id, rv_id, rating, comment, created_at) " +
            "VALUES (#{user_id}, #{rv_id}, #{rating}, #{comment}, NOW())")
    void insertReview(ReviewDTO reviewDTO);

    @Delete("DELETE FROM review WHERE review_id = #{review_id}")
    void deleteReview(@Param("review_id") int review_id);

    //숙소 상세페이지에서 해당하는 숙소의 리뷰 조회
    @Select("SELECT r.review_id, r.rv_id, r.user_id, u.nickname, r.rating, r.comment, r.created_at, " +
            "rm.room_name, a.accom_name " +
            "FROM review r " +
            "JOIN user u ON r.user_id = u.user_id " +
            "JOIN reservation res ON r.rv_id = res.rv_id " +
            "JOIN room rm ON res.room_id = rm.room_id " +
            "JOIN accommodation a ON res.accom_id = a.accom_id " +
            "WHERE a.accom_id = #{accom_id}")
    List<ReviewVO> getReviewsByAccomId(@Param("accom_id") int accom_id);
}