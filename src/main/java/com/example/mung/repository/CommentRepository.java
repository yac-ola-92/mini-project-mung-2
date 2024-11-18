package com.example.mung.repository;

import com.example.mung.domain.CommentDTO;
import com.example.mung.entity.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // 모든 댓글 가져오기 (likeCount와 dislikeCount 포함)
    @Query(value = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname," +
            "COALESCE(SUM(CASE WHEN cl.type = 'LIKE' THEN 1 ELSE 0 END), 0) AS likeCount, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'DISLIKE' THEN 1 ELSE 0 END), 0) AS dislikeCount " +
            "FROM comment c " +
            "JOIN `user` u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_like cl ON c.comment_id = cl.comment_id " +
            "GROUP BY c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname", nativeQuery = true)
    List<Comment> getAllComment();

    // 특정 유저가 작성한 댓글 목록 조회 (likeCount와 dislikeCount 포함)
    @Query(value = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'LIKE' THEN 1 ELSE 0 END), 0) AS likeCount, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'DISLIKE' THEN 1 ELSE 0 END), 0) AS dislikeCount " +
            "FROM comment c " +
            "JOIN `user` u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_like cl ON c.comment_id = cl.comment_id " +
            "WHERE c.user_id = :user_id " +
            "GROUP BY c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname", nativeQuery = true)
    List<Comment> getCommentByUserId(@Param("user_id") int user_id);

    // 특정 게시물에 달린 댓글 목록 조회 (likeCount와 dislikeCount 포함)
    @Query(value = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'LIKE' THEN 1 ELSE 0 END), 0) AS likeCount, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'DISLIKE' THEN 1 ELSE 0 END), 0) AS dislikeCount " +
            "FROM comment c " +
            "JOIN `user` u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_like cl ON c.comment_id = cl.comment_id " +
            "WHERE c.post_id = :post_id " +
            "GROUP BY c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname", nativeQuery = true)
    List<Comment> getCommentsByPostId(@Param("post_id") int post_id);

    // 댓글 ID로 댓글 조회
    @Query(value = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'LIKE' THEN 1 ELSE 0 END), 0) AS likeCount, " +
            "COALESCE(SUM(CASE WHEN cl.type = 'DISLIKE' THEN 1 ELSE 0 END), 0) AS dislikeCount " +
            "FROM comment c " +
            "JOIN `user` u ON c.user_id = u.user_id " +
            "LEFT JOIN comment_like cl ON c.comment_id = cl.comment_id " +
            "WHERE c.comment_id = :comment_id " +
            "GROUP BY c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.nickname", nativeQuery = true)
    Comment findById(@Param("comment_id") int comment_id);

    // 댓글 수정
    @Transactional
    @Modifying
    @Query(value = "UPDATE comment c SET c.content = :content, c.created_at = CURRENT_TIMESTAMP " +
            "WHERE c.comment_id = :commentId AND c.post_id = :postId", nativeQuery = true)
    int updateComment(@Param("content") String content, @Param("commentId") int commentId, @Param("postId") int postId);

    // 댓글 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Comment c WHERE c.comment_id = :commentId", nativeQuery = true)
    int deleteComment(int commentId);
}
