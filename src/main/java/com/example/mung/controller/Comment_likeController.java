package com.example.mung.controller;

import com.example.mung.entity.Comment;
import com.example.mung.entity.Comment_like;
import com.example.mung.entity.User;
import com.example.mung.service.Comment_likeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class Comment_likeController {

    private final Comment_likeService commentLikeService;

    @Autowired
    public Comment_likeController(Comment_likeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    // 좋아요/싫어요 처리
    @PostMapping("/{type}/{comment_id}")
    public ResponseEntity<?> likeOrDislike(@PathVariable String type, @PathVariable int comment_id, HttpSession session) {
        User userInfo = (User) session.getAttribute("userInfo");
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 사용할 수 있습니다.");
        }

        Comment_like commentLike = new Comment_like();
        commentLike.setComment(new Comment());  // 댓글 객체 생성
        commentLike.getComment().setCommentId(comment_id);  // 댓글 ID 설정

        commentLike.setUser(new User());  // 사용자 객체 생성
        commentLike.getUser().setUserId(userInfo.getUserId());  // 사용자 ID 설정

        if ("like".equalsIgnoreCase(type)) {
            commentLike.setType(Comment_like.Type.LIKE);
        } else if ("dislike".equalsIgnoreCase(type)) {
            commentLike.setType(Comment_like.Type.DISLIKE);
        } else {
            return ResponseEntity.badRequest().body("잘못된 타입입니다.");
        }

        // 좋아요/싫어요 처리 후 결과 반환
        Map<String, Integer> response = commentLikeService.likeOrDislike(commentLike);
        return ResponseEntity.ok(response);
    }

    // 댓글의 좋아요/싫어요 수 조회
    @GetMapping("/{commentId}/count")
    public ResponseEntity<Map<String, Integer>> getCounts(@PathVariable int commentId) {
        Map<String, Integer> counts = commentLikeService.getLikeAndDislikeCounts(commentId);
        return ResponseEntity.ok(counts);
    }
}
