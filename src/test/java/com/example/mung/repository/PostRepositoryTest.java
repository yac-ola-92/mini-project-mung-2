package com.example.mung.repository;
import com.example.mung.entity.Post;
import com.example.mung.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    public void findAllPosts() {
        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertFalse(posts.isEmpty());
    }

//    @Test
//    @DisplayName("카테고리별 게시글 조회 테스트")
//    public void getPostByCategory() {
//        Post.Category category = Post.Category.rec;
//        List<Post> posts = postRepository.getPostByCategory(category.name());
//        int count = posts.size();
//        assertNotNull(posts);
//        assertFalse(posts.isEmpty());
//        System.out.println(category + ": " + count);
//        posts.forEach(post -> System.out.println(post.getCategory()));
//        assertTrue(posts.stream().allMatch(post -> post.getCategory() == category),
//                "All posts should have the category: " + category);
//    }

    @Test
    @DisplayName("게시글 등록 테스트")
    public void insertPostTest() throws IOException {

        UserEntity user = userRepository.findById(1);

        if (user == null) {
            fail("User with id 1 not found");
        }

        Post post = new Post();
        post.setUser(user);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setCategory(Post.Category.rec);
        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());
        post.setPassword("0123");
        post.setView_count(0);

        // 테스트용 가짜 byte[] 데이터를 설정
        byte[] fakeFileData = new byte[10];
        fakeFileData[0] = 1;
        fakeFileData[1] = 2;
        post.setFiles(fakeFileData);

        // 게시글 삽입
        int rowsAffected = postRepository.insert(
                (long) post.getUser().getUser_id(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().name(),
                post.getCreated_at(),
                post.getUpdated_at(),
                post.getPassword(),
                post.getView_count(),
                Arrays.toString(post.getFiles())  // byte[]로 전달
        );
        assertTrue(rowsAffected > 0, "게시글이 성공적으로 등록되어야 합니다.");
    }

    @Autowired
    private EntityManager entityManager;
    @Test
    @DisplayName("조회수 증가 테스트")
    public void increaseViewCount() {
        Integer postId = 35;

        Post postBeforeUpdate = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        int initialViewCount = postBeforeUpdate.getView_count();
        int rowsAffected = postRepository.increaseViewCount(postId);
        entityManager.flush();
        entityManager.clear();
        Post postAfterUpdate = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        int updatedViewCount = postAfterUpdate.getView_count();
        assertEquals(initialViewCount + 1, updatedViewCount, "조회수가 1 증가해야 합니다.");
        assertEquals(1, rowsAffected, "조회수를 증가시키는 쿼리가 1개의 행을 업데이트해야 합니다.");
    }

}
