package com.example.mung.mapper;

import com.example.mung.domain.LoginDTO;
import com.example.mung.domain.UserVO;

public class UserMapperToLogin {

    public static UserVO toVO (LoginDTO dto){
        return new UserVO(dto.getUser_login_id(),dto.getPassword());
    }

    public static LoginDTO toDTO (UserVO vo){
        return new LoginDTO(vo.getUser_loginId(),vo.getPassword(),vo.getUser_name());
    }


}
