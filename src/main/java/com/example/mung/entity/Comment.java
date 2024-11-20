package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 댓글 작성자

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment_like> likes; // 좋아요/싫어요 목록

    @Transient
    public long getLikeCount() {
        // 좋아요 타입만 필터링하여 카운트
        return likes.stream()
                .filter(like -> like.getType() == Comment_like.Type.LIKE)
                .count();
    }

    @Transient
    public long getDislikeCount() {
        // 싫어요 타입만 필터링하여 카운트
        return likes.stream()
                .filter(like -> like.getType() == Comment_like.Type.DISLIKE)
                .count();
    }
}
