package com.example.mung.service;

import com.example.mung.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll(); // 모든 댓글 조회

    List<Comment> readByUserId(int userId); // 특정 유저가 작성한 댓글 조회

    List<Comment> readByPostId(int postId); // 특정 게시물에 달린 댓글 조회

    Comment findById(int commentId); // 댓글 ID로 댓글 조회

    boolean register(Comment comment); // 댓글 생성

    boolean modify(Comment comment); // 댓글 수정

    boolean remove(int commentId, int user); // 댓글 삭제

    int getCommentCountByPostId(int postId); // 댓글 갯수
}