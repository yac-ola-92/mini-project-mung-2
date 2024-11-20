package com.example.mung.repository;

import com.example.mung.entity.Comment;
import com.example.mung.entity.Comment_like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Comment_likeRepository extends JpaRepository<Comment_like, Integer> {
    // 좋아요/싫어요 추가
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO comment_like (commentId, user_id, type) " +
            "VALUES (:commentId, :userId, :type)", nativeQuery = true)
    int insertLikeDislike(@Param("commentId") int commentId,
                          @Param("userId") int userId,
                          @Param("type") String type);

    long countByCommentAndType(Comment comment, Comment_like.Type type);

    // 좋아요/싫어요 업데이트 (변경 시)
    @Modifying
    @Transactional
    @Query(value = "UPDATE comment_like SET type = :type WHERE commentId = :commentId AND user_id = :userId", nativeQuery = true)
    int updateLikeDislike(@Param("type") String type,
                          @Param("commentId") int commentId,
                          @Param("userId") int userId);


    // 특정 사용자가 특정 댓글에 대한 좋아요/싫어요 상태 확인
    @Query(value = "SELECT * FROM comment_like WHERE comment_id = :commentId AND user_id = :userId", nativeQuery = true)
    List<Comment_like> findByCommentIdAndUserId(@Param("commentId") int commentId, @Param("userId") int userId);

    // 특정 댓글의 좋아요/싫어요 카운트
    @Query(value = "SELECT COUNT(*) FROM comment_like WHERE comment_id = :commentId AND type = 'LIKE'", nativeQuery = true)
    int getLikeCount(@Param("commentId") int commentId);

    @Query(value = "SELECT COUNT(*) FROM comment_like WHERE comment_id = :commentId AND type = 'DISLIKE'", nativeQuery = true)
    int getDislikeCount(@Param("commentId") int commentId);

}
