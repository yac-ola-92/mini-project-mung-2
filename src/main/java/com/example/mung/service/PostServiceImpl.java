package com.example.mung.service;

import com.example.mung.entity.Post;
import com.example.mung.exception.PostNotFoundException;
import com.example.mung.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getPostsByCategory(String category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable);
    }

    @Override
    public boolean modify(Post post) {
        Post existingPost = postRepository.findById(post.getPost_id())
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        // 수정 데이터 적용
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdated_at(LocalDateTime.now());
        existingPost.setFiles(post.getFiles());

        postRepository.save(existingPost); // 저장
        return true;
    }

    @Override
    public boolean removeWithPassword(int post_id, String password) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        // 비밀번호 확인
        if (!post.getPassword().equals(password)) {
            return false; // 비밀번호가 일치하지 않으면 false 반환
        }
        // 삭제 처리
        postRepository.delete(post);
        return true;
    }

    @Override
    public boolean checkPassword(int post_id, String password) {
        String storedPassword = postRepository.checkPostPassword(post_id);
        return storedPassword != null && storedPassword.equals(password);
    }

    @Override
    public boolean createPost(Post post) {
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean remove(int post_id) {
        int rowsAffected = postRepository.deleteByPostId(post_id);
        return rowsAffected > 0;
    }

    @Override
    public boolean increaseViewCount(int post_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        post.setView_count(post.getView_count() + 1);
        postRepository.save(post);
        return true;
    }

    @Override
    public Page<Post> searchByTitle(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }

    @Override
    public Page<Post> searchByContent(String keyword, Pageable pageable) {
        return postRepository.findByContentContaining(keyword, pageable);
    }

    @Override
    public Page<Post> searchByNickname(String nickname, Pageable pageable) {
        return postRepository.findByUser_NicknameContaining(nickname, pageable);
    }

    @Override
    public Post readById(int post_id) {
        Post post = postRepository.findById(post_id).orElse(null);
        if (post != null) {
            // Lazy Loading 방지
            post.getUser().getUser_id();
        }
        return post;
    }

}
