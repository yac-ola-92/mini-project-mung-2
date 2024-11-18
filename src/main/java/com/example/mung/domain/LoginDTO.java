package com.example.mung.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDTO {

    private final String user_login_id;
    private final String password;
    private String phone;

    // 기본 생성자 추가
    public LoginDTO() {
        this.user_login_id = null;
        this.password = null;
    }

    public LoginDTO(String user_login_id, String password) {
        this.user_login_id = user_login_id;
        this.password = password;
    }

    public LoginDTO(String user_login_id, String password, String phone) {
        this.user_login_id = user_login_id;
        this.password = password;
        this.phone = phone;
    }
}

