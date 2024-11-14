package com.example.mung.controller;

import com.example.mung.domain.CommentDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 특정 게시물의 댓글 리스트 조회
    @GetMapping("/post/{post_id}/comments")
    public String getCommentsByPostId(@PathVariable int post_id, Model model) {
        List<CommentDTO> comments = commentService.readByPostId(post_id);
        model.addAttribute("comments", comments);
        return "comments/post"; // 댓글 목록을 표시하는 템플릿
    }

    // 댓글 추가
    @PostMapping("/post/{post_id}/comments/add")
    public String addComment(@PathVariable int post_id, @ModelAttribute CommentDTO commentDTO, HttpSession session) {
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");
        if (userInfo == null) {
            session.setAttribute("message", "로그인 후에 댓글을 작성할 수 있습니다.");
            return "redirect:/login";
        }

        // 댓글 작성자 정보 추가
        commentDTO.setUser_id(userInfo.getUser_id());
        commentDTO.setPost_id(post_id);

        try {
            // 댓글 저장 처리
            commentService.register(commentDTO);
            session.setAttribute("message", "댓글이 성공적으로 추가되었습니다.");
        } catch (DataIntegrityViolationException e) {
            logger.error("댓글 저장 중 오류 발생.", e);
            session.setAttribute("message", "댓글 저장에 실패했습니다. 입력 값을 확인해주세요.");
            return "redirect:/post/" + post_id;
        } catch (Exception e) {
            logger.error("예상치 못한 오류 발생", e);
            session.setAttribute("message", "예상치 못한 오류가 발생했습니다.");
            return "redirect:/error";
        }

        return "redirect:/post/" + post_id; // 성공 시 게시물 페이지로 리다이렉트
    }

    @PostMapping("/post/comments/delete/{commentId}")
    @ResponseBody
    public Map<String, Object> deleteComment(@PathVariable int commentId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");

        if (userInfo == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        try {
            boolean isDeleted = commentService.remove(commentId, userInfo.getUser_id()); // user_id 전달
            response.put("success", isDeleted);

            // 성공 여부에 따라 메시지 설정
            if (isDeleted) {
                response.put("message", "댓글이 성공적으로 삭제되었습니다.");
            } else {
                response.put("message", "댓글 삭제에 실패했습니다.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }

        return response;
    }

    @PostMapping("/post/comments/update/{commentId}")
    @ResponseBody
    public Map<String, Object> updateComment(@PathVariable int commentId, @RequestBody CommentDTO commentDTO, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        UserVO userInfo = (UserVO) session.getAttribute("userInfo");

        if (userInfo == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        try {
            // 본인의 댓글만 수정 가능하도록 추가 확인
            CommentDTO existingComment = commentService.findById(commentId);
            if (existingComment == null || existingComment.getUser_id() != userInfo.getUser_id()) {
                response.put("success", false);
                response.put("message", "자신의 댓글만 수정할 수 있습니다.");
                return response;
            }

            // 댓글 ID와 user_id 설정
            commentDTO.setComment_id(commentId);
            commentDTO.setUser_id(userInfo.getUser_id()); // user_id 추가 설정

            // 수정 요청 실행
            boolean isUpdated = commentService.modify(commentDTO);

            // 로그 추가
            logger.info("Comment ID: " + commentId + " 수정 요청 결과: " + isUpdated);

            response.put("success", isUpdated);
            if (isUpdated) {
                response.put("message", "댓글이 성공적으로 수정되었습니다.");
            } else {
                response.put("message", "댓글 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            logger.error("댓글 수정 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "댓글 수정 중 오류가 발생했습니다.");
        }
        return response;
    }

}
