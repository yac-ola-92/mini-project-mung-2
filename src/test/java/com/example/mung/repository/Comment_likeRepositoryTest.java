package com.example.mung.repository;

import com.example.mung.entity.Comment_like;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Comment_likeRepositoryTest {

    @Autowired
    private Comment_likeRepository commentLikeRepository;

    private int commentId;
    private int userId;
    @Autowired
    private Comment_likeRepository comment_likeRepository;

    @BeforeEach
    public void setup() {
        commentId = 1;
        userId = 1;
    }

    @Test
    @Transactional
    @DisplayName("좋아요/싫어요 추가 테스트")
    public void testInsertLikeDislike() {
        // Given: test용 데이터 준비
        int commentId = 1;
        int userId = 1;
        String type = "LIKE";
        int result = comment_likeRepository.insertLikeDislike(commentId, userId, type);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("좋아요/싫어요 업데이트 (변경 시) 테스트")
    public void testUpdateLikeDislike() {
        int commentId = 1;
        int userId = 1;
        String newType = "DISLIKE";
        int result = commentLikeRepository.updateLikeDislike(newType, commentId, userId);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("특정 사용자가 특정 댓글에 대한 좋아요/싫어요 상태 확인 테스트")
    void testFindByCommentIdAndUserId() {
        List<Comment_like> result = commentLikeRepository.findByCommentIdAndUserId(1, 1);
        assertThat(result.size()).isGreaterThanOrEqualTo(1);
        assertThat(result.get(0).getType()).isEqualTo(Comment_like.Type.DISLIKE);
    }

    @Test
    void testDisplayLikeAndDislikeCount() {
        int likeCount = commentLikeRepository.getLikeCount(1);
        int dislikeCount = commentLikeRepository.getDislikeCount(1);

        System.out.println("좋아요 개수: " + likeCount);
        System.out.println("싫어요 개수: " + dislikeCount);
    }

}

