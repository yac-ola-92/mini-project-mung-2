package com.example.mung.service;

import com.example.mung.entity.Comment_like;
import com.example.mung.repository.Comment_likeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
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
        // 이미 해당 댓글에 좋아요/싫어요를 눌렀는지 확인
        List<Comment_like> existingLikes = commentLikeRepository.findByCommentIdAndUserId(commentLike.getComment().getCommentId(), commentLike.getUser().getUser_id());

        if (existingLikes.isEmpty()) {
            // 새로운 좋아요/싫어요 추가
            commentLikeRepository.save(commentLike);
        } else {
            // 이미 존재하는 좋아요/싫어요 상태가 있으면 업데이트
            Comment_like existingLike = existingLikes.get(0);
            existingLike.setType(commentLike.getType());
            commentLikeRepository.save(existingLike);
        }

        // 좋아요/싫어요 수 카운트 반환
        Map<String, Integer> counts = new HashMap<>();
        counts.put("likeCount", commentLikeRepository.getLikeCount(commentLike.getComment().getCommentId()));
        counts.put("dislikeCount", commentLikeRepository.getDislikeCount(commentLike.getComment().getCommentId()));

        return counts;
    }


    @Override
    public Map<String, Integer> getLikeAndDislikeCounts(int commentId) {
        int likeCount = commentLikeRepository.getLikeCount(commentId);
        int dislikeCount = commentLikeRepository.getDislikeCount(commentId);

        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("dislikeCount", dislikeCount);

        return response;
    }
}
