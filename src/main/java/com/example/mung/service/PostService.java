package com.example.mung.service;

import com.example.mung.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll(); // 모든 게시글 조회

    List<Post> findAll(int page, int size); // 페이징 처리된 게시글 조회

    boolean modify(Post post); // 게시글 수정

    boolean increaseViewCount(int post_id); // 게시글 조회수 증가

    boolean remove(int post_id); // 게시글 삭제

    boolean createPost(Post post); // 게시글 작성

    List<Post> searchByTitle(String keyword); // 제목으로 검색

    List<Post> searchByContent(String keyword); // 내용으로 검색

    List<Post> searchByNickname(String nickname); // 닉네임으로 검색

    boolean checkPassword(int post_id, String password); // 비밀번호 확인

    Post readById(int post_id); // ID로 게시글 조회

    List<Post> getPostsByCategory(String category); // 카테고리별 게시글 조회
}
