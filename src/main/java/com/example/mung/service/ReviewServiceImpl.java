package com.example.mung.service;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import com.example.mung.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<ReviewVO> getAllReviews() {
        return reviewMapper.getAllReviews();
    }

    @Override
    public void createReview(ReviewDTO reviewDTO) {
        // ReviewDTO를 받아 ReviewVO로 매핑하거나 직접 사용하여 저장
        reviewMapper.insertReview(reviewDTO);
    }
}
