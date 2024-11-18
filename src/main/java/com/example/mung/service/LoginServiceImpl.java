
package com.example.mung.service;

import com.example.mung.domain.LoginDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.entity.UserEntity;
import com.example.mung.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository repository;

    // 현재 인증된 사용자 정보를 반환
    public UserVO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // UserVO가 UserDetails를 구현한 객체일 경우
            return (UserVO) authentication.getPrincipal();
        }
        return null;
    }


    @Override
    public UserVO loginSuccess(LoginDTO dto) {
        System.out.println("loginSuccess 성공");
        System.out.println(dto);
        UserEntity user = repository.getUserByLoginIdAndPassword(dto.getUser_login_id());
        System.out.println(user);
        if (user == null) {
            throw new RuntimeException("Invalid username or password");  // 또는 다른 예외 처리
        }

        UserVO vo = new UserVO(user);  // UserEntity 객체를 UserVO로 변환
        System.out.println(vo);
        return vo;
    }

    @Override
    public boolean idCheck(String user_loginId){
            boolean flag = repository.checkId(user_loginId) > 0;
        return flag;
    }

    @Override
    public String printName(String user_loginId){

        return repository.getUserNameByLoginId(user_loginId);
    }

    @Override
    public List<String> idList(){

        return repository.idList();
    }

    @Override
    public String findId(String name, String email, LocalDateTime birth){
        return repository.checkIdByNameEmailBirth(name,email,birth);
    }


    @Override
    public String idCheckForModifyPassword(String id, String email, LocalDateTime birth) {
        return repository.checkIdByIdEmailBirth(id,email,birth);
    }

    @Override
    public int updatePassword(LoginDTO dto) {
        repository.updatePasswordByLoginId(dto.getUser_login_id(),dto.getPassword(),dto.getPhone());
        return 1;
    }

    @Override
    public String findIdByUserPhone(String phone){
        System.out.println("findIdByUserPhone");
        System.out.println(phone);
   return  repository.checkPhoneByLoginId(phone);
    }

//    public UserVO loginCheck(LoginDTO dto) {
//        UserVO user = mapper.findByUserLoginId(dto.getUser_login_id());
//        if (user == null || mapper.findByUserLoginId(dto.getUser_login_id()) != null) {
//            throw new RuntimeException("아이디와 비밀번호를 확인하세요.");
//        }
//        return user;
//    }
//
//    @Override
//    public UserVO findUserLoginId(String id){
//        System.out.println("Service단 : findByUserId실행");
//        return mapper.findLoginId(id);
//    }
//
//    @Override
//    public UserVO login(LoginDTO dto){
//        System.out.println("Service단 : login 실행");
//        return mapper.loginActive(dto);
//    }
//



//    @Override
//    public UserVO findUserLoginId(String id) {
//        return null;
//    }
//
//    @Override
//    public UserVO loginService(LoginDTO dto) {
//        return null;
//    }
}

