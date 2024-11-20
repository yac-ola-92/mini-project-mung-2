package com.example.mung.controller;

import com.example.mung.domain.UserVO;
import com.example.mung.entity.Post;
import com.example.mung.entity.UserEntity;
import com.example.mung.service.CommentServiceImpl;
import com.example.mung.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PostController {
    @Autowired
    private final PostService postService;
    @Autowired
    private CommentServiceImpl commentServiceImpl;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private UserEntity getLoginUser(HttpSession session) {
        Object userInfo = session.getAttribute("userInfo");

        if (userInfo instanceof UserEntity) {
            return (UserEntity) userInfo;
        } else if (userInfo instanceof UserVO) {
            // UserVO를 UserEntity로 변환
            return new UserEntity((UserVO) userInfo);
        } else {
            return null; // 세션에 유효한 사용자 정보가 없을 경우
        }
    }

    // UserVO를 UserEntity로 변환하는 메서드
    private UserEntity convertToUserEntity(UserVO userVO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(userVO.getUser_id());
        userEntity.setUser_login_id(userVO.getUser_loginId());
        // 필요한 다른 필드들도 매핑
        return userEntity;
    }

    // 게시판 메인
    @GetMapping("/postMain")
    public String postMain(HttpSession session, Model model, Pageable pageable) {
        UserEntity userInfo = getLoginUser(session);
        Pageable paged = PageRequest.of(pageable.getPageNumber(), 5);

        // Pageable을 사용하여 페이징 처리된 게시글 가져오기
        Page<Post> posts = postService.findAllPosts(paged);

        // 댓글 갯수 조회 (각 게시글에 대해 댓글 갯수 조회)
        Map<Integer, Integer> commentCounts = new HashMap<>();
        for (Post post : posts) {
            commentCounts.put(post.getPost_id(), commentServiceImpl.getCommentCountByPostId(post.getPost_id()));
        }
        model.addAttribute("posts", posts); // Page<Post>를 그대로 전달
        model.addAttribute("userInfo", userInfo); // 로그인된 사용자 정보 추가
        model.addAttribute("commentCounts", commentCounts); // 댓글 갯수 추가

        return "postMain";
    }

    // 검색 기능
    @GetMapping("/posts/search")
    public String searchPosts(@RequestParam("keyword") String keyword,
                              @RequestParam("type") String type,
                              Pageable pageable,
                              Model model) {
        Page<Post> posts;

        switch (type) {
            case "title":
                posts = postService.searchByTitle(keyword, pageable);
                break;
            case "content":
                posts = postService.searchByContent(keyword, pageable);
                break;
            case "nickname":
                posts = postService.searchByNickname(keyword, pageable);
                break;
            default:
                posts = Page.empty(pageable);
        }

        Map<Integer, Integer> commentCounts = new HashMap<>();
        for (Post post : posts) {
            commentCounts.put(post.getPost_id(), commentServiceImpl.getCommentCountByPostId(post.getPost_id()));
        }

        model.addAttribute("posts", posts);
        model.addAttribute("commentCounts", commentCounts);
        return "postMain";
    }

    // 게시글 작성 페이지로 이동 (GET 요청 처리)
    @GetMapping("/new")
    public String postWritePage(HttpSession session, Model model) {
        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        model.addAttribute("userInfo", userInfo);
        return "postWrite";
    }

    // 게시글 작성 처리 (POST 요청 처리)
    @PostMapping("/new")
    public String createPost(@ModelAttribute @Valid Post post,
                             BindingResult bindingResult,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "postWrite";
        }
        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }

        post.setUser(userInfo); // 작성자 정보 설정
        if (file != null && !file.isEmpty()) {
            post.setFiles(file.getBytes());
        }
        if (postService.createPost(post)) {
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 작성되었습니다!");
            return "redirect:/postMain";
        }
        redirectAttributes.addFlashAttribute("error", "게시글 작성에 실패했습니다.");
        return "redirect:/postMain";
    }

    // 이미지 업로드 처리
    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String uploadDir = "C:/uploads/";
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());
            return "/uploads/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/posts")
    public String getPosts(Pageable pageable, Model model) {
        Page<Post> page = postService.findAllPosts(pageable);
        model.addAttribute("page", page);
        model.addAttribute("posts", page.getContent());
        return "postMain"; // 게시글 리스트를 보여줄 뷰
    }

    @GetMapping("/posts/category/{category}")
    public String getPostsByCategory(@PathVariable String category, Pageable pageable, Model model) {
        Pageable paged = PageRequest.of(pageable.getPageNumber(), 5);
        Page<Post> page = postService.getPostsByCategory(category, paged);

        // 댓글 갯수 조회
        Map<Integer, Integer> commentCounts = new HashMap<>();
        for (Post post : page) {
            commentCounts.put(post.getPost_id(), commentServiceImpl.getCommentCountByPostId(post.getPost_id()));
        }

        model.addAttribute("commentCounts", commentCounts); // 댓글 갯수 추가
        model.addAttribute("posts", page); // Page<Post> 객체 전달
        return "postMain";
    }


    @GetMapping("/post/{post_id}")
    public String getPostDetail(@PathVariable int post_id, Model model, HttpSession session) {
        postService.increaseViewCount(post_id); // 조회수 증가
        Post post = postService.readById(post_id); // 최신 데이터 조회
        if (post == null) {
            return "error/404"; // 게시글이 없는 경우
        }
        // userInfo 추가
        UserEntity userInfo = getLoginUser(session);
        model.addAttribute("userInfo", userInfo);
        if (post.getFiles() != null) {
            String mimeType = "image/jpeg"; // 파일 타입 변경 가능
            String base64Image = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(post.getFiles());
            model.addAttribute("base64Image", base64Image);
        }
        model.addAttribute("post", post);
        return "postDetail";
    }

    // 게시글 수정 페이지로 이동
    @GetMapping("/update/{post_id}")
    public String updatePostPage(@PathVariable int post_id, HttpSession session, Model model) {
        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        Post post = postService.readById(post_id);
        if (!post.getUser().getNickname().equals(userInfo.getNickname())) {
            model.addAttribute("error", "작성자만 게시글을 수정할 수 있습니다.");
            return "postDetail";
        }
        model.addAttribute("post", post);
        return "postUpdate";  // 게시글 수정 페이지로 이동
    }

    @PostMapping("/update/{post_id}")
    public String updatePost(
            @PathVariable int post_id,
            @Valid Post post,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session,
            Model model) {
        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        // DB에서 게시글을 조회
        Post existingPost = postService.readById(post_id);
        if (existingPost == null) {
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "postUpdate";
        }
        // 게시글 데이터를 모델에 추가
        model.addAttribute("post", existingPost);  // 수정할 게시글을 모델에 추가

        // 비밀번호 검증 로직을 삭제 로직과 동일하게 수정
        if (!postService.checkPassword(post_id, post.getPassword())) {
            model.addAttribute("error");
            return "postUpdate";
        }
        // 기존 카테고리를 유지하도록 설정
        if (post.getCategory() == null) {
            post.setCategory(existingPost.getCategory());
        }
        // 파일 처리 및 게시글 수정 로직
        if (file != null && !file.isEmpty()) {
            try {
                post.setFiles(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
                return "postUpdate";
            }
        }
        boolean isUpdated = postService.modify(post);
        if (isUpdated) {
            session.setAttribute("successMessage", "수정이 완료되었습니다.");
            return "redirect:/post/" + post_id;  // 수정한 게시글 상세 페이지로 이동
        } else {
            model.addAttribute("error", "게시글 수정에 실패했습니다.");
            return "postUpdate";
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    // 게시글 삭제 페이지로 이동
    @GetMapping("/post/delete/{post_id}")
    public String deletePostPage(@PathVariable int post_id, HttpSession session, Model model) {
        logger.info("삭제 페이지 요청. post_id: {}", post_id);

        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            logger.warn("로그인된 사용자가 없습니다.");
            return "redirect:/login";
        }

        Post post = postService.readById(post_id);
        if (post == null) {
            logger.warn("게시글을 찾을 수 없습니다. post_id: {}", post_id);
            return "redirect:/postMain";
        }

        if (post.getUser() == null) {
            logger.error("게시글 작성자 정보가 없습니다. post_id: {}", post_id);
            return "redirect:/postMain";
        }

        if (!post.getUser().getNickname().equals(userInfo.getNickname())) {
            logger.warn("권한 없는 사용자가 삭제 페이지에 접근했습니다. post_id: {}, 사용자: {}", post_id, userInfo.getNickname());
            return "redirect:/postMain";
        }

        model.addAttribute("post_id", post_id);
        logger.info("삭제 페이지로 이동 준비 완료. post_id: {}", post_id);
        return "postDelete";
    }


    // 비밀번호 확인 후 게시글 삭제 처리
    @PostMapping("/post/delete/{post_id}")
    public String deletePost(@PathVariable int post_id,
                             @RequestParam("password") String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        UserEntity userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        if (postService.checkPassword(post_id, password)) {
            postService.remove(post_id);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/postMain";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 틀렸습니다.");
            return "redirect:/post/delete/" + post_id;
        }
    }

    @PostMapping("/post/checkPassword")
    @ResponseBody
    public Map<String, Boolean> checkPassword(@RequestBody Map<String, String> payload) {
        String password = payload.get("password");
        int post_id = Integer.parseInt(payload.get("post_id"));
        boolean isValid = postService.checkPassword(post_id, password);  // 비밀번호 확인
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);  // 응답으로 valid 값 전달
        return response;
    }
}