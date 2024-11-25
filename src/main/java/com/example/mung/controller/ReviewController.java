package com.example.mung.controller;

import com.example.mung.domain.ReviewDTO;
import com.example.mung.domain.ReviewVO;
import com.example.mung.domain.UserVO;
import com.example.mung.service.LoginService;
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
    private final LoginService loginService;

    @GetMapping("/mypage/reviews")
    public String getUserReviews(HttpSession session, Model model) {
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");

        if (userInfo == null) {
            logger.debug("User not logged in, redirecting to login.");
            return "redirect:/login";
        }

        int userId = userInfo.getUser_id();
        logger.debug("Fetching reviews for userId: {}", userId);

        try {
            List<ReviewVO> reviews = reviewService.getReviewsByUserId(userId);

            if (reviews == null || reviews.isEmpty()) {
                logger.debug("No reviews found for userId: {}", userId);
                model.addAttribute("noReviews", true); // 추가된 부분
            } else {
                logger.debug("Fetched reviews: {}", reviews.size());
            }

            model.addAttribute("reviews", reviews);
        } catch (Exception e) {
            logger.error("Error fetching reviews: {}", e.getMessage());
            model.addAttribute("error", "리뷰 데이터를 불러오는 중 오류가 발생했습니다.");
        }
        return "postReview";
    }

    @GetMapping("/reviews/create")
    public String getCreateReviewPage(HttpSession session, Model model) {
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");
        if (userInfo == null) {
            return "redirect:/login";
        }
        return "createReview";
    }

    @PostMapping("/reviews/create")
    public String handleCreateReview(@RequestParam(required = false) Integer rvId,
                                     @RequestParam(required = false) String reviewContent,
                                     HttpSession session, Model model) {
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");
        int userId = userInfo.getUser_id();
        if (userId < 0) {
            return "redirect:/login";
        }

        if (rvId == null) {
            model.addAttribute("error", "예약 내역이 없습니다.");
            return "createReview";
        }

        if (reviewContent == null || reviewContent.trim().isEmpty()) {
            model.addAttribute("error", "리뷰 내용을 입력해주세요.");
            model.addAttribute("rv_id", rvId); // 오류 발생 시 예약 ID 전달
            return "createReview";
        }

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUser_id(userInfo.getUser_id());
        reviewDTO.setRv_id(rvId);
        reviewDTO.setComment(reviewContent);
        reviewService.createReview(reviewDTO);

        return "redirect:/mypage/reviews";
    }

    @DeleteMapping("/{review_id}")
    @ResponseBody
    public ResponseEntity<String> deleteReview(@PathVariable int review_id, HttpSession session) {
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");
        int userId = userInfo.getUser_id();
        if (userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ReviewVO review = reviewService.getReviewById(review_id);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 리뷰를 찾을 수 없습니다.");
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
