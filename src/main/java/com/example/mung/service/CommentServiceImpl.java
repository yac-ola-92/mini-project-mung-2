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

    // 특정 유저의 댓글 조회
    @Override
    public List<CommentDTO> readByUserId(int user_id) {
        return commentMapper.getCommentByUserId(user_id);
    }

    // 특정 게시글의 댓글 조회
    @Override
    public List<CommentDTO> readByPostId(int post_id) {
        return commentMapper.getCommentsByPostId(post_id);
    }

    // 댓글 등록
    @Override
    public boolean register(CommentDTO comment) {
        return commentMapper.insertComment(comment) > 0;
    }

    // 댓글 수정
    @Override
    public boolean modify(CommentDTO comment) {
        return commentMapper.updateComment(comment) > 0;
    }

    // 댓글 삭제 (트랜잭션 적용)
    @Transactional
    @Override
    public boolean remove(int comment_id, int user_id) {
        return commentMapper.deleteComment(comment_id, user_id) > 0;
    }


    // ID로 댓글 찾기
    @Override
    public CommentDTO findById(int comment_id) {
        return commentMapper.findById(comment_id);
    }
}
