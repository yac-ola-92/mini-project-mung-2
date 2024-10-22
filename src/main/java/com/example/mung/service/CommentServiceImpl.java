package com.example.mung.service;

import com.example.mung.domain.CommentDTO;
import com.example.mung.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    // 모든 댓글 조회
    @Override
    public List<CommentDTO> findAll() {
        return commentMapper.getAllComment();
    }

    // 특정 사용자가 작성한 댓글 조회
    @Override
    public List<CommentDTO> readByUserId(int user_id) {
        return commentMapper.getCommentByUserId(user_id);
    }

    // 특정 게시글에 달린 댓글 조회
    @Override
    public List<CommentDTO> readByPostId(int post_id) {
        return commentMapper.getCommentsByPostId(post_id);
    }

    // 댓글 등록
    @Transactional
    @Override
    public boolean register(CommentDTO comment) {
        return commentMapper.insertComment(comment) > 0;  // 성공 여부를 확인
    }

    // 대댓글 등록 및 댓글 수정
    @Transactional
    @Override
    public boolean modify(CommentDTO comment) {
        return commentMapper.updateComment(comment) > 0;  // 댓글 수정
    }

    // 댓글 삭제
    @Transactional
    @Override
    public boolean remove(int comment_id) {
        // 대댓글이 있는지 확인 후, 부모 댓글 삭제 시 대댓글도 함께 삭제
        commentMapper.deleteReplyByParent(comment_id);  // 대댓글 삭제
        return commentMapper.deleteComment(comment_id) > 0;  // 부모 댓글 삭제
    }

    // 댓글 ID로 댓글 조회
    @Override
    public CommentDTO findById(int comment_id) {
        return commentMapper.findById(comment_id);
    }
}
