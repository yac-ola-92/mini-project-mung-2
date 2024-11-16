package com.example.mung.service;

import com.example.mung.entity.Post;
import com.example.mung.exception.PostNotFoundException;
import com.example.mung.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAll(int page, int size) {
        return postRepository.findAll().stream()
                .skip((page - 1) * size)
                .limit(size)
                .toList(); // Stream API로 페이징 처리
    }

    @Override
    public boolean modify(Post post) {
        Post existingPost = postRepository.findById(post.getPost_id())
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdated_at(post.getUpdated_at());
        postRepository.save(existingPost);
        return true;
    }

    @Override
    public boolean createPost(Post post) {
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean remove(int post_id) {
        if (postRepository.existsById(post_id)) {
            postRepository.deleteById(post_id);
            return true;
        }
        return false;
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
    public List<Post> searchByTitle(String keyword) {
        return postRepository.findByTitle(keyword);
    }

    @Override
    public List<Post> searchByContent(String keyword) {
        return postRepository.findByContent(keyword);
    }

    @Override
    public List<Post> searchByNickname(String nickname) {
        return postRepository.findByNickname(nickname);
    }

    @Override
    public boolean checkPassword(int post_id, String password) {
        String storedPassword = postRepository.findByPostId(post_id);
        return storedPassword != null && storedPassword.equals(password);
    }

    @Override
    public Post readById(int post_id) {
        return postRepository.findById(post_id)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public List<Post> getPostsByCategory(String category) {
        return postRepository.getPostByCategory(category);
    }
}
