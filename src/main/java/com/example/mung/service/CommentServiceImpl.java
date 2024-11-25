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

//    @Override
//    @Transactional
//    public boolean remove(int commentId, int user) {
//        return commentRepository.deleteComment(commentId) > 0; // 삭제된 행 수가 0보다 클 경우 성공
//    }
@Override
@Transactional
public boolean remove(int commentId, int userId) {
    // 댓글 조회
    Comment comment = commentRepository.findById(commentId);
    if (comment == null || comment.getUser().getUser_id() != userId) {
        return false; // 댓글이 없거나, 삭제 권한이 없는 경우
    }
    // 댓글 삭제
    return commentRepository.deleteComment(commentId) > 0;
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
