package com.example.mung.controller;

import com.example.mung.domain.UserVO;
import com.example.mung.entity.Comment;
import com.example.mung.entity.Comment_like;
import com.example.mung.entity.UserEntity;
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

    @Autowired
    private Comment_likeService commentLikeService;

    // 세션에서 사용자 정보를 가져오는 메서드 추가
    private UserEntity getLoginUser(HttpSession session) {
        Object userInfo = session.getAttribute("userInfo");

        if (userInfo instanceof UserEntity) {
            return (UserEntity) userInfo;
        } else if (userInfo instanceof UserVO) {
            // UserVO를 UserEntity로 변환
            return convertToUserEntity((UserVO) userInfo);
        } else {
            return null; // 세션에 유효한 사용자 정보가 없을 경우
        }
    }

    // UserVO를 UserEntity로 변환하는 메서드
    private UserEntity convertToUserEntity(UserVO userVO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(userVO.getUser_id());
        userEntity.setUser_login_id(userVO.getUser_loginId());
        // 필요한 다른 필드들도 매핑
        return userEntity;
    }

    @PostMapping("/{type}/{commentId}")
    public ResponseEntity<?> likeOrDislike(@PathVariable String type, @PathVariable int commentId, HttpSession session) {
        try {
            UserEntity userInfo = getLoginUser(session);  // 로그인된 사용자 정보 확인
            System.out.println("[DEBUG] userInfo: " + userInfo);
            System.out.println("[DEBUG] commentId: " + commentId);

            if (userInfo == null) {
                // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
            }

            // 좋아요/싫어요 처리 로직
            Comment_like commentLike = new Comment_like();
            commentLike.setComment(new Comment());  // 댓글 객체 생성
            commentLike.getComment().setCommentId(commentId);  // 댓글 ID 설정
            commentLike.setUser(userInfo);  // 사용자 정보 설정

            if ("like".equalsIgnoreCase(type)) {
                commentLike.setType(Comment_like.Type.LIKE);
            } else if ("dislike".equalsIgnoreCase(type)) {
                commentLike.setType(Comment_like.Type.DISLIKE);
            } else {
                return ResponseEntity.badRequest().body("잘못된 타입입니다.");
            }

            // 좋아요/싫어요 결과 처리
            Map<String, Integer> response = commentLikeService.likeOrDislike(commentLike);
            System.out.println("[DEBUG] Response: " + response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();  // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 댓글의 좋아요/싫어요 수 조회
    @GetMapping("/{commentId}/count")
    public ResponseEntity<Map<String, Integer>> getCounts(@PathVariable int commentId) {
        Map<String, Integer> counts = commentLikeService.getLikeAndDislikeCounts(commentId);
        return ResponseEntity.ok(counts);
    }
}
