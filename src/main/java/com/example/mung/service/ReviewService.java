package com.example.mung.service;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;

import java.util.List;

public interface ReviewService {
    List<ReviewVO> getAllReviews();
    void createReview(ReviewDTO reviewDTO);
    void deleteReview(int review_id);
    List<ReviewVO> getReviewsByUserId(int user_id);
    ReviewVO getReviewById(int review_id);
    List<ReviewVO> getReviewsByAccomId(int accom_id);
}


