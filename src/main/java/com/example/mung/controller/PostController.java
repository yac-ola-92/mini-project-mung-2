package com.example.mung.controller;

import com.example.mung.domain.PostDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    private UserVO getLoginUser(HttpSession session) {
        return (UserVO) session.getAttribute("userInfo");
    }

    // 게시판 메인
    @GetMapping("/postMain")
    public String postMain(HttpSession session, Model model) {
        UserVO userInfo = getLoginUser(session);
        List<PostDTO> posts = postService.findAll();
        model.addAttribute("posts", posts);
        if (userInfo != null) {
            model.addAttribute("userInfo", userInfo);  // 로그인된 사용자 정보 추가
        }
        return "postMain";
    }

    // 카테고리별 게시글 조회
    @GetMapping("/posts/category/{category}")
    public String getPostsByCategory(@PathVariable("category") String category, Model model) {
        List<PostDTO> posts = postService.getPostsByCategory(category);
        model.addAttribute("posts", posts);
        return "postMain";
    }

    // 검색 기능
    @GetMapping("/posts/search")
    public String searchPosts(@RequestParam("keyword") String keyword,
                              @RequestParam("type") String type, Model model) {
        List<PostDTO> posts;
        if (type.equals("title")) {
            posts = postService.searchByTitle(keyword);
        } else if (type.equals("content")) {
            posts = postService.searchByContent(keyword);
        } else if (type.equals("nickname")) {
            posts = postService.searchByNickname(keyword);
        } else {
            posts = new ArrayList<>();
        }
        model.addAttribute("posts", posts);
        return "postMain";
    }

    // 게시글 작성 페이지로 이동 (GET 요청 처리)
    @GetMapping("/new")
    public String postWritePage(HttpSession session, Model model) {
        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        model.addAttribute("userInfo", userInfo);
        return "postWrite";
    }

    // db에 이미지파일의 실제 데이터가 저장되지 않고 이미지 경로만 저장.
    // 게시글 작성 처리 (POST 요청 처리)
    @PostMapping("/new")
    public String createPost(@ModelAttribute @Valid PostDTO postDTO,
                             BindingResult bindingResult,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             HttpSession session) throws IOException {
        if (bindingResult.hasErrors()) {
            return "postWrite";
        }
        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        postDTO.setUser_id(userInfo.getUser_id());
        postDTO.setNickname(userInfo.getNickname());
        if (file != null && !file.isEmpty()) {
            postDTO.setFiles(file.getBytes());
            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : "jpeg";
            postDTO.setFileType(fileType);
        }
        if (postService.createPost(postDTO)) {
            return "redirect:/postMain";
        }
        return "postWrite";
    }

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
            Files.createDirectories(filePath.getParent()); // 디렉토리가 없으면 생성
            file.transferTo(filePath.toFile()); // 파일 저장
            return "/uploads/" + fileName; // 저장된 파일 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 게시글 조회수 증가포함
    @GetMapping("/post/{post_id}")
    public String getPostDetail(@PathVariable int post_id, Model model, HttpSession session) {
        postService.increaseViewCount(post_id); // 조회수 증가 호출

        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message"); // 세션에서 메시지 제거
        }

        PostDTO post = postService.readById(post_id);
        if (post == null) {
            return "error/404";
        }

        // 수정 완료 메시지가 있으면 모델에 추가
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage"); // 한 번 표시한 후 세션에서 제거
        }

        if (post.getFiles() != null) {
            String mimeType = "image/" + (post.getFileType() != null ? post.getFileType() : "jpeg");
            String base64Image = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(post.getFiles());
            model.addAttribute("base64Image", base64Image);
        }
        model.addAttribute("post", post);

        // 로그인된 사용자 정보 추가
        UserVO userInfo = getLoginUser(session);
        model.addAttribute("userInfo", userInfo);

        return "postDetail";
    }

    // 게시글 수정 페이지로 이동
    @GetMapping("/update/{post_id}")
    public String updatePostPage(@PathVariable int post_id, HttpSession session, Model model) {
        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        PostDTO post = postService.readById(post_id);
        if (!post.getNickname().equals(userInfo.getNickname())) {
            model.addAttribute("error", "작성자만 게시글을 수정할 수 있습니다.");
            return "postDetail";
        }
        model.addAttribute("post", post);
        return "postUpdate";  // 게시글 수정 페이지로 이동
    }

    // 게시글 수정 처리 (POST)
    @PostMapping("/update/{post_id}")
    public String updatePost(
            @PathVariable int post_id,
            @Valid PostDTO postDTO,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session,
            Model model) {

        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }

        // DB에 있는 게시글 정보 조회
        PostDTO existingPost = postService.readById(post_id);
        if (existingPost == null) {
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "postUpdate";
        }

        // 비밀번호 검증 로직을 삭제 로직과 동일하게 수정
        if (!postService.checkPassword(post_id, postDTO.getPassword())) {
            model.addAttribute("error", "비밀번호가 틀립니다.");
            return "postUpdate";
        }

        // 기존 카테고리를 유지하도록 설정
        if (postDTO.getCategory() == null) {
            postDTO.setCategory(existingPost.getCategory());
        }

        // 파일 처리 및 게시글 수정 로직
        if (file != null && !file.isEmpty()) {
            try {
                postDTO.setFiles(file.getBytes());
                postDTO.setFileType(file.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
                return "postUpdate";
            }
        }

        boolean isUpdated = postService.modify(postDTO);
        if (isUpdated) {
            session.setAttribute("successMessage", "수정이 완료되었습니다.");
            return "redirect:/post/" + post_id;  // 수정한 게시글 상세 페이지로 이동
        } else {
            model.addAttribute("error", "게시글 수정에 실패했습니다.");
            return "postUpdate";
        }
    }


    // 게시글 삭제 페이지로 이동
    @GetMapping("/post/delete/{post_id}")
    public String deletePostPage(@PathVariable int post_id, HttpSession session, Model model) {
        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        PostDTO post = postService.readById(post_id);
        if (post == null || !post.getNickname().equals(userInfo.getNickname())) {
            return "redirect:/postMain"; // 작성자가 아닌 경우 리디렉션
        }
        model.addAttribute("post_id", post_id); // post_id 전달
        return "postDelete";
    }

    // 비밀번호 확인 후 게시글 삭제 처리
    @PostMapping("/post/delete/{post_id}/confirm")
    public String deletePost(@PathVariable int post_id, @RequestParam("password") String password, HttpSession session) {
        UserVO userInfo = getLoginUser(session);
        if (userInfo == null) {
            return "redirect:/login";
        }
        if (postService.checkPassword(post_id, password)) {
            postService.remove(post_id);
            return "redirect:/postMain";
        } else {
            return "redirect:/post/delete/{post_id}"; // 비밀번호 틀리면 다시 삭제 페이지로 리디렉션
        }
    }

    @PostMapping("/post/checkPassword")
    @ResponseBody
    public Map<String, Boolean> checkPassword(@RequestBody Map<String, String> payload) {
        String password = payload.get("password");
        int postId = Integer.parseInt(payload.get("post_id"));

        boolean isValid = postService.checkPassword(postId, password);  // 비밀번호 확인
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);  // 응답으로 valid 값 전달
        return response;
    }

}