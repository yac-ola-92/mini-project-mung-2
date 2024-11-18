package com.example.mung.service;

import com.example.mung.entity.Comment_like;
import com.example.mung.repository.Comment_likeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class Comment_likeServiceImpl implements Comment_likeService {

    private final Comment_likeRepository commentLikeRepository;

    @Autowired
    public Comment_likeServiceImpl(Comment_likeRepository commentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
    }

    @Override
    @Transactional
    public Map<String, Integer> likeOrDislike(Comment_like commentLike) {
        // 기존에 좋아요/싫어요가 있는지 확인
        Comment_like existing = commentLikeRepository.findByCommentIdAndUserId(commentLike.getComment().getCommentId(), commentLike.getUser().getUser_id()).stream().findFirst().orElse(null);

        if (existing != null) {
            // 존재하면 업데이트
            commentLikeRepository.updateLikeDislike(commentLike.getType().name(), commentLike.getComment().getCommentId(), commentLike.getUser().getUser_id());
        } else {
            // 없으면 새로 추가
            commentLikeRepository.insertLikeDislike(commentLike.getComment().getCommentId(), commentLike.getUser().getUser_id(), commentLike.getType().name());
        }

        // 최신 좋아요/싫어요 카운트 반환
        return getLikeAndDislikeCounts(commentLike.getComment().getCommentId());
    }

    @Override
    public Map<String, Integer> getLikeAndDislikeCounts(int comment_id) {
        int likeCount = commentLikeRepository.getLikeCount(comment_id);
        int dislikeCount = commentLikeRepository.getDislikeCount(comment_id);

        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("dislikeCount", dislikeCount);

        return response;
    }
}
