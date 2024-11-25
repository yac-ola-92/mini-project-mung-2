package com.example.mung.controller;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import com.example.mung.domain.UserVO;
import com.example.mung.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    // 리뷰 조회 페이지 렌더링
    @GetMapping("/mypage/reviews")
    public String getUserReviews(HttpSession session, Model model) {
        UserVO user = (UserVO) session.getAttribute("userInfo");
        int userId = user.getUser_id();
        if (userId<0) {
            logger.debug("User not logged in, redirecting to login.");
            return "redirect:/login"; // 로그인되지 않은 경우 리다이렉트
        }

        logger.debug("Session user_id: {}", userId);

        List<ReviewVO> userReviews = reviewService.getReviewsByUserId(userId);
        if (userReviews == null || userReviews.isEmpty()) {
            model.addAttribute("message", "작성된 리뷰가 없습니다.");
        } else {
            model.addAttribute("reviews", userReviews);
        }
        return "postReview";
    }

    @GetMapping("/reviews/create")
    public String createUserReviewPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/login";
        }

        Integer rvId = (Integer) session.getAttribute("rv_id");
        if (rvId == null) {
            model.addAttribute("error", "예약 내역이 없습니다.");
            return "redirect:/mypage/reviews";
        }
        model.addAttribute("rv_id", rvId);
        return "createReview"; // HTML 렌더링
    }

    // 리뷰 등록 처리
    @PostMapping("/reviews/create")
    @ResponseBody
    public ResponseEntity<String> createReview(@RequestBody ReviewDTO reviewDTO, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        reviewDTO.setUser_id(userId);
        reviewService.createReview(reviewDTO);
        return ResponseEntity.ok("리뷰가 등록되었습니다.");
    }

    // 리뷰 삭제 처리
    @DeleteMapping("/reviews/{review_id}")
    @ResponseBody
    public ResponseEntity<String> deleteReview(@PathVariable int review_id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ReviewVO review = reviewService.getReviewById(review_id); // 리뷰 정보를 가져옴
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 리뷰를 찾을 수 없습니다.");
        }

        if (review.getUser_id() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        try {
            reviewService.deleteReview(review_id);
            logger.debug("Review successfully deleted: {}", review_id);
            return ResponseEntity.ok("리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            logger.error("Error deleting review: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류가 발생했습니다.");
        }
    }
}
