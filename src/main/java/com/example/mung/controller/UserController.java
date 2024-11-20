package com.example.mung.controller;

import com.example.mung.domain.UserDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.domain.transfer.PetInfo;
import com.example.mung.entity.UserEntity;
import com.example.mung.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class UserController {


      @Autowired
      private UserService service;


    @GetMapping("/myPage")
    public String goMyPage(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "/mainPage";
        } else {

            System.out.println("myPage 이동");
            return "/myPage";
        }
    }
//변경
    @GetMapping("/userInformation")
    public String goUserInformation(HttpSession session) {

        if (session.getAttribute("userInfo") == null) {
            return "/mainPage";
        } else {
            System.out.println("userInformation 이동");
            return "/userInformation";
        }

    }

    @Transactional
    @PostMapping("/updateUser")
    public String updateUser(HttpServletRequest request, HttpSession session) {

        UserVO info = (UserVO) session.getAttribute("userInfo");
        System.out.println(info+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        UserVO vo = new UserVO();
        vo.setPet_inform(new PetInfo(request.getParameter("name"), request.getParameter("type"), request.getParameter("age"), request.getParameter("weight")));
        vo.setUser_id(info.getUser_id());

        System.out.println(vo);
        info.setPet_info(vo.getPet_info());
        boolean result = service.modify_pet(info);
        System.out.println(result);
        session.setAttribute("userInfo",info);
        return "redirect:/myPage";
    }

}
