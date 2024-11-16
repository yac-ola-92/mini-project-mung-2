package com.example.mung.service;

import com.example.mung.entity.Comment;
import com.example.mung.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAll() {
        // 모든 댓글 조회
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> readByUserId(int userId) {
        // 특정 유저의 댓글 조회
        return commentRepository.getCommentByUserId(userId);
    }

    @Override
    public List<Comment> readByPostId(int postId) {
        // 특정 게시물의 댓글 조회
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    @Transactional
    public boolean register(Comment comment) {
        // 댓글 등록
        commentRepository.save(comment);
        return true;
    }

    @Override
    public boolean modify(Comment comment) {
        int updatedRows = commentRepository.updateComment(
                comment.getContent(),
                comment.getCommentId(),
                comment.getPost().getPost_id()
        );
        return updatedRows > 0;
    }

    @Override
    public boolean remove(int commentId) {
        int deletedRows = commentRepository.deleteComment(commentId);
        return deletedRows > 0;
    }

    @Override
    public Comment findById(int commentId) {
        // 댓글 ID로 조회
        Optional<Comment> optionalComment = Optional.ofNullable(commentRepository.findById(commentId));
        if (!optionalComment.isPresent()) {
            throw new IllegalStateException("댓글을 찾을 수 없습니다.");
        }
        return optionalComment.get();
    }
}
