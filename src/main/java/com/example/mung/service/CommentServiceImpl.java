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
        return commentRepository.findAll(); // 모든 댓글 조회
    }

    @Override
    public List<Comment> readByUserId(int userId) {
        return commentRepository.getCommentByUserId(userId); // 특정 유저의 댓글 조회
    }

    @Override
    public List<Comment> readByPostId(int postId) {
        return commentRepository.getCommentsByPostId(postId); // 특정 게시물의 댓글 조회
    }

    @Override
    @Transactional
    public boolean register(Comment comment) {
        commentRepository.save(comment); // 댓글 등록
        return true;
    }

    @Override
    public boolean modify(Comment comment) {
        int updatedRows = commentRepository.updateComment(
                comment.getContent(),
                comment.getCommentId(),
                comment.getPost().getPost_id()
        );
        return updatedRows > 0; // 수정된 행 수가 0보다 클 경우 성공
    }

    @Override
    @Transactional
    public boolean remove(int commentId, int user) {
        return commentRepository.deleteComment(commentId) > 0; // 삭제된 행 수가 0보다 클 경우 성공
    }

    @Override
    public Comment findById(int commentId) {
        Optional<Comment> optionalComment = Optional.ofNullable(commentRepository.findById(commentId));
        if (!optionalComment.isPresent()) {
            throw new IllegalStateException("댓글을 찾을 수 없습니다.");
        }
        return optionalComment.get();
    }

    @Override
    public int getCommentCountByPostId(int postId) {
        return commentRepository.countByPostId(postId);
    }
}
