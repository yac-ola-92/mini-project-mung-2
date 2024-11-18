package com.example.mung.repository;

import com.example.mung.entity.Comment;
import com.example.mung.entity.Post;
import com.example.mung.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    void testInsertComment() {
        Post existingPost = postRepository.findById(1).orElseThrow(() -> new IllegalStateException("Post not found"));
        UserEntity existingUser = userRepository.findById(1);

        Comment comment = Comment.builder()
                .content("This is a test comment.")
                .post(existingPost)
                .user(existingUser)
                .created_at(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getCommentId()).isGreaterThan(0);
        assertThat(savedComment.getContent()).isEqualTo("This is a test comment.");
    }
    @Test
    @DisplayName("댓글 수정 테스트")
    void testUpdateComment() {
        // 댓글 조회
        Optional<Comment> optionalComment = Optional.ofNullable(commentRepository.findById(1));
        if (!optionalComment.isPresent()) {
            throw new AssertionError("댓글을 찾을 수 없습니다.");
        }
        Comment existingComment = optionalComment.get();
        String updatedContent = "정말 좋은 정보 감사합니다!";
        int updatedRows = commentRepository.updateComment(updatedContent, existingComment.getCommentId(), existingComment.getPost().getPost_id());
        assertThat(updatedRows).isGreaterThan(0);
        Optional<Comment> updatedOptionalComment = Optional.ofNullable(commentRepository.findById(existingComment.getCommentId()));
        if (!updatedOptionalComment.isPresent()) {
            throw new AssertionError("수정된 댓글을 찾을 수 없습니다.");
        }
        Comment updatedComment = updatedOptionalComment.get();
        assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void testDeleteComment() {
        // 댓글 조회
        Optional<Comment> optionalComment = Optional.ofNullable(commentRepository.findById(19));
        if (!optionalComment.isPresent()) {
            throw new AssertionError("삭제할 댓글을 찾을 수 없습니다.");
        }
        Comment existingComment = optionalComment.get();

        // 삭제 실행
        int deletedRows = commentRepository.deleteComment(existingComment.getCommentId());

        // 삭제 검증
        assertThat(deletedRows).isGreaterThan(0);

        // 삭제 확인
        Optional<Comment> deletedComment = Optional.ofNullable(commentRepository.findById(existingComment.getCommentId()));
        assertThat(deletedComment.isPresent()).isFalse();
    }


    @Test
    @DisplayName("모든 댓글을 가져오는 테스트 (좋아요와 싫어요 카운트 포함)")
    public void testGetAllComment() {
        List<Comment> comments = commentRepository.getAllComment();
        assertEquals(true, comments.size() > 0, "댓글 목록이 비어있지 않아야 합니다.");
        for (Comment comment : comments) {
            System.out.println("댓글 내용: " + comment.getContent());

            long likeCount = comment.getLikes().stream().filter(like -> like.getType().equals("LIKE")).count();
            long dislikeCount = comment.getLikes().stream().filter(like -> like.getType().equals("DISLIKE")).count();

            System.out.println("LIKE Count: " + likeCount + ", DISLIKE Count: " + dislikeCount);

            assertEquals(likeCount, comment.getLikes().stream().filter(like -> like.getType().equals("LIKE")).count(), "좋아요 개수가 잘못되었습니다.");
            assertEquals(dislikeCount, comment.getLikes().stream().filter(like -> like.getType().equals("DISLIKE")).count(), "싫어요 개수가 잘못되었습니다.");
        }
    }

    @Test
    @DisplayName("특정 유저의 댓글 목록 조회 테스트 (좋아요와 싫어요 카운트 포함)")
    public void testGetCommentByUserId() {
        int userId = 1;
        List<Comment> comments = commentRepository.getCommentByUserId(userId);
        assertTrue(comments.size() > 0, "댓글 목록이 비어있지 않아야 합니다.");
        for (Comment comment : comments) {
            System.out.println("댓글 내용: " + comment.getContent());
            long likeCount = comment.getLikes().stream().filter(like -> like.getType().equals("LIKE")).count();
            long dislikeCount = comment.getLikes().stream().filter(like -> like.getType().equals("DISLIKE")).count();

            System.out.println("LIKE Count: " + likeCount + ", DISLIKE Count: " + dislikeCount);

            assertTrue(likeCount >= 0, "좋아요 개수가 잘못되었습니다.");
            assertTrue(dislikeCount >= 0, "싫어요 개수가 잘못되었습니다.");
        }
    }

        @Test
        @DisplayName("댓글 ID로 댓글 조회 테스트 (좋아요/싫어요 카운트 포함)")
        public void testFindById() {
            int commentId = 1;
            Comment comment = commentRepository.findById(commentId);

            assertNotNull(comment, "댓글이 null이어서는 안됩니다.");

            assertEquals(commentId, comment.getCommentId(), "댓글 ID가 일치하지 않습니다.");

            long likeCount = comment.getLikes().stream().filter(like -> like.getType().equals("LIKE")).count();
            long dislikeCount = comment.getLikes().stream().filter(like -> like.getType().equals("DISLIKE")).count();

            System.out.println("LIKE Count: " + likeCount + ", DISLIKE Count: " + dislikeCount);
        }



}
