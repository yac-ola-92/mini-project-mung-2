package com.example.mung.service;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import java.util.List;

public interface ReviewService {
    List<ReviewVO> getAllReviews(); // 리뷰 목록 가져오기
    void createReview(ReviewDTO reviewDTO); // 리뷰 등록
}
