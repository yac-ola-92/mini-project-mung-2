package com.example.mung.service;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import com.example.mung.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewVO> getAllReviews() {
        return reviewMapper.getAllReviews();
    }

    @Override
    public void createReview(ReviewDTO reviewDTO) {
        reviewMapper.insertReview(reviewDTO);
    }

    @Override
    public void deleteReview(int review_id) {
        reviewMapper.deleteReview(review_id);
    }

    @Override
    public List<ReviewVO> getReviewsByUserId(int user_id) {
        return reviewMapper.getReviewsByUserId(user_id);
    }

    @Override
    public ReviewVO getReviewById(int review_id) {
        return reviewMapper.getReviewById(review_id);
    }

    @Override
    public List<ReviewVO> getReviewsByAccomId(int accom_id){
        return reviewMapper.getReviewsByAccomId(accom_id);
    }
}
