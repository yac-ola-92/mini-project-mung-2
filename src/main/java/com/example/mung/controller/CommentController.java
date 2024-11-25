package com.example.mung.controller;

import com.example.mung.domain.UserVO;
import com.example.mung.entity.Comment;
import com.example.mung.entity.Post;
import com.example.mung.entity.UserEntity;
import com.example.mung.service.CommentServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentServiceImpl commentService;

    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    // 특정 게시물의 댓글 리스트 조회
    @GetMapping("/post/{post_id}/comments")
    public String getCommentsByPostId(@PathVariable int post_id, Model model) {
        List<Comment> comments = commentService.readByPostId(post_id);
        model.addAttribute("comments", comments);
        return "comments/post"; // 댓글 목록을 표시하는 템플릿
    }

    // 댓글 추가
    @PostMapping("/post/{post_id}/comments/add")
    public String addComment(
            @PathVariable int post_id,
            @ModelAttribute Comment comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 확인
        Object sessionUser = session.getAttribute("userInfo");
        if (sessionUser == null || !(sessionUser instanceof UserVO)) {
            redirectAttributes.addFlashAttribute("message", "로그인 후에 댓글을 작성할 수 있습니다.");
            return "redirect:/login";
        }

        try {
            // UserVO -> UserEntity 변환
            UserVO userVO = (UserVO) sessionUser;
            UserEntity userEntity = new UserEntity(userVO);

            // 댓글 작성자 및 게시물 정보 설정
            comment.setUser(userEntity);
            Post post = new Post();
            post.setPost_id(post_id);
            comment.setPost(post);

            // 댓글 내용 검증
            if (comment.getContent() == null || comment.getContent().isBlank()) {
                redirectAttributes.addFlashAttribute("message", "댓글 내용을 입력해주세요.");
                return "redirect:/post/" + post_id;
            }

            // 댓글 저장 처리
            commentService.register(comment);
            redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 추가되었습니다.");
        } catch (DataIntegrityViolationException e) {
            logger.error("DB 제약 조건 위반", e);
            redirectAttributes.addFlashAttribute("message", "댓글 저장에 실패했습니다. 입력 값을 확인해주세요.");
        } catch (Exception e) {
            logger.error("예상치 못한 오류 발생", e);
            redirectAttributes.addFlashAttribute("message", "예상치 못한 오류가 발생했습니다.");
        }

        return "redirect:/post/" + post_id;
    }

    @PostMapping("/post/comments/delete/{commentId}")
    @ResponseBody
    public Map<String, Object> deleteComment(@PathVariable int commentId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 세션에서 사용자 정보 확인
            UserEntity userInfo = (UserEntity) session.getAttribute("userInfo");
            if (userInfo == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return response;
            }

            logger.info("댓글 삭제 요청. commentId: {}, userId: {}", commentId, userInfo.getUser_id());

            // 댓글 정보 조회
            Comment comment = commentService.findById(commentId);
            if (comment == null) {
                response.put("success", false);
                response.put("message", "댓글이 존재하지 않습니다.");
                return response;
            }

            // 댓글 작성자 확인
            if (!Objects.equals(comment.getUser().getUser_id(), userInfo.getUser_id())) {
                response.put("success", false);
                response.put("message", "댓글 삭제 권한이 없습니다.");
                return response;
            }


            // 댓글 삭제 처리
            boolean isDeleted = commentService.remove(commentId, userInfo.getUser_id());
            if (isDeleted) {
                response.put("success", true);
                response.put("message", "댓글이 성공적으로 삭제되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "댓글 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            logger.error("댓글 삭제 중 오류 발생. commentId: {}", commentId, e);
            response.put("success", false);
            response.put("message", "삭제 중 예상치 못한 오류가 발생했습니다.");
        }

        return response;
    }

}
