package com.example.mung.service;

import com.example.mung.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Page<Post> findAllPosts(Pageable pageable);

    Page<Post> getPostsByCategory(String category, Pageable pageable);

    boolean modify(Post post); // 게시글 수정

    boolean increaseViewCount(int post_id); // 게시글 조회수 증가

    boolean remove(int post_id); // 게시글 삭제

    boolean createPost(Post post); // 게시글 작성

    Page<Post> searchByTitle(String keyword, Pageable pageable);
    Page<Post> searchByContent(String keyword, Pageable pageable);
    Page<Post> searchByNickname(String nickname, Pageable pageable);

    boolean checkPassword(int post_id, String password); // 비밀번호 확인

    boolean removeWithPassword(int post_id, String password); // 비밀번호 확인 후 삭제

    Post readById(int post_id); // ID로 게시글 조회

}
