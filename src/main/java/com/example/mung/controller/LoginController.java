
package com.example.mung.controller;

import com.example.mung.domain.LoginDTO;
import com.example.mung.domain.UserDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.domain.transfer.PetInfo;
import com.example.mung.domain.transfer.Role;
import com.example.mung.service.LoginService;
import com.example.mung.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginController {

    //user 정보
    @Autowired
    private UserService userService;
    //로그인한 사람의 정보 추출 경로
    //login 서비스
    @Autowired
    private LoginService loginService;

    private final PasswordEncoder passwordEncoder;

    // 생성자 주입 방식
    @Autowired
    public LoginController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    //로그인 페이지로 이동하면서 idList 긁어오기
    @GetMapping("/login")
    public String loginView(Model model, HttpSession session) {
        if (session.getAttribute("userInfo") != null) {
            System.out.println("세션에 담긴 값입니다." + session.getAttribute("userInfo"));
            System.out.println("이미 로그인 되어있습니다.");
            return "redirect:/mainPage";  // 메인 페이지로 리다이렉트
        }
        model.addAttribute("idList", loginService.idList());
        System.out.println("로그인 화면 출력");
        return "/login";
    }

    @GetMapping("/sms")
    public String sms(){
        return "/sms";
    }


//    //로그인 성공시 session에 로그인 정보 담기
//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> login(HttpSession session, @RequestBody LoginDTO dto) {
//        UserVO user = loginService.loginSuccess(dto);
//
//        if (user != null) {
//            // 세션에 사용자 정보 저장
//            session.setAttribute("userInfo", user);
//
//            // JWT 토큰 생성
//            String token = Jwts.builder()
//                    .setSubject(user.getUser_loginId())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
//                    .signWith(SignatureAlgorithm.HS256, "secretKey") // 비밀키로 서명
//                    .compact();
//
//            // 응답 데이터 생성
//            Map<String, Object> response = new HashMap<>();
//            response.put("token", token);
//            response.put("message", "Login successful");
//
//            // 서버에서 JWT 토큰과 메시지 반환
//            return ResponseEntity.ok(response);
//        } else {
//            // 로그인 실패 시 처리
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Invalid login credentials");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }


    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        session.invalidate();
        System.out.println("로그아웃 성공");

        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer; // 이전 페이지로 리다이렉트
        } else {
            return "redirect:/mainPage"; // 기본 페이지로 리다이렉트
        }
    }

    @PostMapping("/api/findIdByPhone")
    @ResponseBody
    public ResponseEntity<String> findIdByPhone(@RequestBody Map<String, String> requestData) {
        String phone = requestData.get("phone");
        String realPhone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        System.out.println(realPhone);

        String foundId = loginService.findIdByUserPhone(realPhone);
        System.out.println(foundId);
        if (foundId != null) {
            return ResponseEntity.ok(foundId); // 찾은 아이디 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found");
        }
    }

    //아이디 찾기 기능
    @PostMapping("/findId")
    @ResponseBody
    public ResponseEntity<String> findId(@RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        String email = requestData.get("email");
        String birth = requestData.get("birth");

        String year = "";
        String month = "";
        String day = "";
        int count = Integer.parseInt(birth.substring(0, 2));
        if (count < 50) {

            year = "20" + birth.substring(0, 2); // "20" + "02"

        } else {
            year = "19" + birth.substring(0, 2);
        }
        month = birth.substring(2, 4); // "08"
        day = birth.substring(4, 6); // "19"

        String date = year + "-" + month + "-" + day;
        System.out.println(date);
        System.out.println(name + email + birth);
        LocalDate formatBirth = LocalDate.parse(date);
        System.out.println(formatBirth);
        LocalDateTime localDateTime = formatBirth.atStartOfDay(); // LocalDateTime으로 변환

        System.out.println(localDateTime);


        String foundId = loginService.findId(name, email, localDateTime);

        System.out.println(foundId);
        if (foundId != null) {
            return ResponseEntity.ok(foundId); // 찾은 아이디 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID not found");
        }
    }

    //비밀번호 찾기 기능
    @PostMapping("/idCheckForModifyPassword")
    @ResponseBody
    public ResponseEntity<String> idCheckForModifyPassword(@RequestBody Map<String, String> requestData) {

        String id = requestData.get("id");
        String email = requestData.get("email");
        String birth = requestData.get("birth");

        String year = "";
        String month = "";
        String day = "";
        int count = Integer.parseInt(birth.substring(0, 2));
        if (count < 50) {

            year = "20" + birth.substring(0, 2); // "20" + "02"

        } else {
            year = "19" + birth.substring(0, 2);
        }
        month = birth.substring(2, 4); // "08"
        day = birth.substring(4, 6); // "19"

        String date = year + "-" + month + "-" + day;
        System.out.println(date);
        System.out.println(id + email + birth);

        // LocalDate로 변환
        LocalDate formatBirth = LocalDate.parse(date);
        System.out.println(formatBirth);
        LocalDateTime localDateTime = formatBirth.atStartOfDay(); // LocalDateTime으로 변환

        System.out.println("변형된 생년월일"+localDateTime);

        String foundName = loginService.idCheckForModifyPassword(id, email, localDateTime);

        System.out.println(foundName);
        if (foundName != null) {
            return ResponseEntity.ok(foundName + "님 의 \n비밀번호를 수정합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾지 못 했습니다.");
        }
    }

    //비밀번호 찾은 후 수정 기능
    @Transactional
    @PostMapping("/updatePassword")
    @ResponseBody
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestData) {
        String id = requestData.get("id");
        System.out.println(id);
        String newPassword = requestData.get("newPassword");
        System.out.println(newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        System.out.println(encodedPassword);
        String phone = requestData.get("phone");
        String realPhone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        System.out.println(realPhone);
        LoginDTO dto = new LoginDTO(id,encodedPassword,realPhone);
        System.out.println(dto);
        int result;
        try {
            result = loginService.updatePassword(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 수정 중 오류 발생");
        }

        if (result > 0) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾지 못 했습니다.");
        }
    }

    //일반 회원가입 기능
    @PostMapping("/generalJoin")
    @Transactional
    @ResponseBody
    public ResponseEntity<String> generalJoin(@RequestBody Map<String, String> dto) {
        try {
            // 비밀번호 인코딩
            String encodedPassword = passwordEncoder.encode(dto.get("password"));

            // 생년월일 처리
            String year = "";
            String month = "";
            String day = "";

            String userBirth = dto.get("user_birth");
            if (userBirth != null && userBirth.length() == 6) {
                int count = Integer.parseInt(userBirth.substring(0, 2));
                if (count < 50) {
                    year = "20" + userBirth.substring(0, 2);
                } else {
                    year = "19" + userBirth.substring(0, 2);
                }
                month = userBirth.substring(2, 4);
                day = userBirth.substring(4, 6);
            } else {
                // 잘못된 생년월일 형식 처리
                return ResponseEntity.badRequest().body("Invalid birth date format");
            }

            String date = year + "-" + month + "-" + day;

            // UserVO 객체에 값 설정
            UserVO vo = new UserVO();
            Role roles = new Role(new String[]{"USER","HOST"});

            vo.setUser_name(dto.get("user_name"));
            vo.setUser_email(dto.get("user_email"));
            vo.setPassword(encodedPassword);
            vo.setUser_phone(dto.get("user_phone"));
            vo.setUser_birthToString(date);
            vo.setUser_gender(dto.get("user_gender"));
            vo.setRole(roles);
            vo.setNickname(dto.get("nickname"));
            vo.setUser_loginId(dto.get("user_loginId"));

            System.out.println(vo+"aaaaaaaaaaaaaaa");
            // 사용자 등록
            boolean result = userService.register(vo);

            if (result) {
                return ResponseEntity.ok("User registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration");
        }
    }



    //사업자 회원가입 기능
    @PostMapping("/businessJoin")
    @Transactional
    public String businessJoin(HttpServletRequest httr) {
        String year = "";
        String month = "";
        String day = "";
        int count = Integer.parseInt(httr.getParameter("user_birth").substring(0, 2));
        if (count < 50) {

            year = "20" + httr.getParameter("user_birth").substring(0, 2); // "20" + "02"

        } else {
            year = "19" + httr.getParameter("user_birth").substring(0, 2);
        }
        month = httr.getParameter("user_birth").substring(2, 4); // "08"
        day = httr.getParameter("user_birth").substring(4, 6); // "19"

        String date = year + "-" + month + "-" + day;

        UserVO vo = new UserVO();
        Role roles = new Role(new String[]{"USER,HOST"});
        vo.setUser_name(httr.getParameter("user_name"));
        vo.setUser_email(httr.getParameter("user_email"));
        vo.setPassword(httr.getParameter("password"));
        vo.setUser_phone(httr.getParameter("user_phone"));
        vo.setUser_birthToString(date);
        vo.setUser_gender(httr.getParameter("user_gender"));
        vo.setRole(roles);
        vo.setNickname(httr.getParameter("nickname"));
        vo.setUser_loginId(httr.getParameter("user_loginId"));
        vo.setBusiness_number(httr.getParameter("business_number"));

        System.out.println(vo);

        boolean result = userService.register_b(vo);

        System.out.println(result);

        return "redirect:/login";
    }

    // .html, .css, .js 파일을 제외한 모든 요청을 index.html로 포워딩
//    @GetMapping("/{path:[^\\.]*}") // 정규식으로 확장자가 포함되지 않은 경로를 선택
//    public String redirect() {
//        return "forward:/index.html"; // index.html로 리디렉션
//    }

}

