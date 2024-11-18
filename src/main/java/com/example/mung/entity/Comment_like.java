package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment_like")
public class Comment_like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment; // 좋아요가 속한 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 좋아요를 누른 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // LIKE 또는 DISLIKE

    public enum Type {
        LIKE, DISLIKE
    }
}
