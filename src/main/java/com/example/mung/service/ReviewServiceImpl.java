package com.example.mung.service;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import com.example.mung.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
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

        List<ReviewVO> reviews = reviewMapper.getReviewsByUserId(user_id);

        // 로그를 추가하여 결과 확인
        if (reviews != null) {

            logger.debug("Fetched reviews count: {}", reviews.size());
            for (ReviewVO review : reviews) {
                logger.debug("Review details: {}", review);
            }
        } else {
            logger.debug("No reviews found for user_id: {}", user_id);
        }

        return reviews;
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
