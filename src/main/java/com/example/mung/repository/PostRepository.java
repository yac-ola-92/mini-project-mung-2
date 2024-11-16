package com.example.mung.repository;
import com.example.mung.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT p.post_id, p.user_id, p.title, p.content, p.category, p.created_at, p.updated_at, p.view_count, u.nickname \" +\n" +
            "            \"FROM post p \" +\n" +
            "            \"JOIN user u ON p.user_id = u.user_id", nativeQuery = true)
    List<Post> findAllPosts();

    @Query(value = "SELECT p.post_id, p.user_id, p.title, p.content, p.category, p.created_at, p.updated_at, p.view_count, u.nickname, p.password, p.files " +
            "FROM post p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "WHERE p.category = :category", nativeQuery = true)
    List<Post> getPostByCategory(@Param("category") String category);

    // 게시글 등록 (첨부파일 경로 포함)
    @Modifying
    @Query(value = "INSERT INTO post (user_id, title, content, category, created_at, updated_at, password, view_count, files) " +
            "VALUES (:user_id, :title, :content, :category, :created_at, :updated_at, :password, :view_count, :files)",
            nativeQuery = true)
    int insert(@Param("user_id") Long user_id,
               @Param("title") String title,
               @Param("content") String content,
               @Param("category") String category,
               @Param("created_at") LocalDateTime createdAt,
               @Param("updated_at") LocalDateTime updatedAt,
               @Param("password") String password,
               @Param("view_count") int viewCount,
               @Param("files") String files);

    // 조회수 증가
    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET view_count = view_count + 1 WHERE post_id = :post_id",
            nativeQuery = true)
    int increaseViewCount(@Param("post_id") int post_id);

    //게시글 제목 검색
    @Query(value = "SELECT p.post_id, p.user_id, p.title, p.content, p.category, p.created_at, p.updated_at, p.view_count, u.nickname, p.password, p.files  " +
            "FROM post p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "WHERE p.title LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Post> findByTitle(@Param("keyword") String keyword);

    // 내용으로 검색
    @Query(value = "SELECT p.post_id, p.user_id, p.title, p.content, p.category, p.created_at, p.updated_at, p.view_count, u.nickname, p.password, p.files " +
                        "FROM post p " +
                       "JOIN user u ON p.user_id = u.user_id " +
                       "WHERE p.content LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Post> findByContent(@Param("keyword") String keyword);

    // 작성자로 검색 (nickname으로 검색)
    @Query(value = "SELECT p.post_id, p.user_id, p.title, p.content, p.category, p.created_at, p.updated_at, p.view_count, u.nickname, p.password, p.files " +
            "FROM post p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "WHERE u.nickname = :nickname", nativeQuery = true)
    List<Post> findByNickname(@Param("nickname") String nickname);

    // 비밀번호 확인
    @Query(value = "SELECT password FROM post WHERE post_id = :post_id", nativeQuery = true)
    String findByPostId(@Param("post_id") int post_id);

}
