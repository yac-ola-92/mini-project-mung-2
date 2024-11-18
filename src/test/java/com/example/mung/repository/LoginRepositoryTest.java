package com.example.mung.repository;

import com.example.mung.domain.UserVO;
import com.example.mung.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class LoginRepositoryTest {

    @Autowired
    private LoginRepository repository;

    @Test
    public void idListTest(){
        List<String>list = repository.idList();
        list.forEach(System.out::println);

    }

    @Test
    public void getUserByLoginIdAndPasswordTest(){
        UserEntity user = repository.getUserByLoginIdAndPassword("host");
        System.out.println(user);

    }

    @Test
    public void checkId(){
        Long check = repository.checkId("host");
        System.out.println(check);
    }

    @Test
    public void getUserNameByLoginId(){
        String check = repository.getUserNameByLoginId("host");
        System.out.println(check);
    }

    @Test
    public void saveUser(){
        UserVO user = new UserVO();
        user.setUser_loginId("host1");
        user.setPassword("1234");
        user.setUser_name("김밥");
        user.setUser_birthToString("2000-01-01");
        user.setUser_gender("여자");
        user.setUser_phone("010-0101-0101");
        user.setUser_email("host@example.com");
        user.setRole("USER,HOST");
        user.setNickname("하하호호");
        UserEntity userEntity = new UserEntity(user);
        System.out.println(repository.save(userEntity));


    }

    @Test
    public void checkIdByNameEmailBirth(){
        UserVO vo = new UserVO();
        vo.setUser_birthToString("1992-08-17");
        LocalDateTime ldt = vo.getUser_birth();
        String result = repository.checkIdByNameEmailBirth("정종민","jjm92@gmail.com",ldt);
        System.out.println(result);
    }



}
