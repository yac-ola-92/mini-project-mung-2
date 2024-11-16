package com.example.mung.controller;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 리뷰 목록을 보여주기 위한 페이지
    @GetMapping("/reviews")
    public String showReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());  // 리뷰 목록
        return "postReview"; // postReview.html 페이지로 이동
    }

    // 리뷰 등록 폼
    @GetMapping("/reviews/new")
    public String showReviewForm(Model model) {
        model.addAttribute("review", new ReviewDTO()); // 빈 ReviewDTO를 모델에 추가
        return "createReview"; // createReview.html 페이지로 이동
    }

    // 리뷰 등록 처리
    @PostMapping("/reviews")
    public String createReview(@ModelAttribute ReviewDTO reviewDTO) {
        reviewService.createReview(reviewDTO); // 리뷰 등록 처리
        return "redirect:/reviews"; // 리뷰 목록으로 리디렉션
    }
}
