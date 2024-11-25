

package com.example.mung.controller;
import com.example.mung.domain.UserVO;
import com.example.mung.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    public void testCreateReview() throws Exception {
        // 로그인된 사용자 정보를 세션에 설정
        UserVO userInfo = new UserVO();
        userInfo.setUser_id(1);  // 로그인한 사용자 ID 설정

        MvcResult result = mockMvc.perform(post("/reviews/create")
                        .param("rv_id", "1")
                        .param("rating", "5")
                        .param("comment", "Great place!")
                        .sessionAttr("userInfo", userInfo))  // 세션에 UserVO 객체 설정
                .andExpect(status().is3xxRedirection())  // 리다이렉션 상태 코드 확인
                .andExpect(redirectedUrl("/mypage/reviews"))  // 리다이렉션 URL 확인
                .andReturn();  // 결과 반환

        // 결과 디버깅
        System.out.println("Response status: " + result.getResponse().getStatus());
        System.out.println("Response content: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserReviews() throws Exception {
        mockMvc.perform(get("/mypage/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("postReview"))
                .andExpect(model().attributeExists("reviews"));
    }
}
