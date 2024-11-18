package com.example.mung.Security;

import com.example.mung.domain.LoginDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.domain.transfer.PetInfo;
import com.example.mung.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
@Slf4j
@CrossOrigin(origins = "http://localhost:8088")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    // 생성자에 loginService 추가
    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil, LoginService loginService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpSession session) {
            log.info("Login attempt with user: " + dto.getUser_login_id());
        try {
            // 사용자 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUser_login_id(), dto.getPassword())
            );

            // 인증 성공 후, 사용자 정보 조회
            UserVO user = loginService.loginSuccess(dto);

            if (user == null) {
                throw new Exception("User not found after successful authentication");
            }

            // 사용자 정보를 세션에 저장
            HashMap<String, String> list = user.getPet_infoList();

            if (list!=null) {
                PetInfo petInfo = new PetInfo(list.get("이름"), list.get("종"), list.get("나이"), list.get("무게"));
                user.setPet_inform(petInfo);
            }
            if (user.getRole() != null && !user.getRole().isEmpty()) {
                user.setRoles(Arrays.asList(user.getRole().split(","))); // 역할을 List로 변환
            }


            session.setAttribute("userInfo", user);  // 세션에 사용자 정보 저장

            // JWT 토큰 생성
            final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUser_login_id());
            final String jwt = jwtUtil.generateToken(userDetails);

            // 성공적인 로그인 후 JWT 토큰 반환
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (BadCredentialsException e) {
            // 인증 실패 시 401 Unauthorized 상태 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (Exception e) {
            // 그 외의 예외 처리 및 로깅
            e.printStackTrace();  // 콘솔에 자세한 예외 정보 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login: " + e.getMessage());
        }
    }

}
