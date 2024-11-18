package com.example.mung.repository;

import com.example.mung.domain.UserVO;
import com.example.mung.domain.transfer.PetInfo;
import com.example.mung.domain.transfer.Role;
import com.example.mung.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void pr() {
        System.out.println("■".repeat(50));
    }
    @AfterEach
    void pr1() {
        System.out.println("■".repeat(50));
    }

    @Test
    @Transactional
    public void findAll(){
        List<UserEntity> list =  repository.findAll();
        List<UserVO> vo = new ArrayList<>();
        for (UserEntity entity : list) {
          vo.add(new UserVO(entity));
        }
        list.forEach(System.out::println);
        System.out.println("●".repeat(50));
        vo.forEach(System.out::println);
    }

    @Test
    public void findById(){
        UserEntity user = repository.findById(2);
        UserVO vo = new UserVO(user);
        System.out.println(user);
        System.out.println("●".repeat(50));
        System.out.println(vo);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void register(){
        UserVO vo = new UserVO();
        vo.setUser_loginId("jjm");
        vo.setPassword("1234");
        vo.setUser_name("정종민");
        vo.setUser_email("jjm92@gmail.com");
        vo.setUser_phone("010-9191-3131");
        vo.setNickname("팥종민");
        vo.setUser_birthToString("1992-08-17");
        vo.setUser_gender ("남자");
        String[] roles = {"USER","HOST"};
        Role role = new Role(roles);
        vo.setRole(role);
        vo.setProfile_image_url("/bbangbbabg.png");
        PetInfo petInfo = new PetInfo("유월이","말티즈","14","6kg");
        vo.setPet_inform(petInfo);
        System.out.println(vo);
        System.out.println("●".repeat(50));
        UserEntity user = new UserEntity(vo);
        System.out.println(user);
        System.out.println("●".repeat(50));
        UserEntity result = repository.save(user);
        System.out.println(result);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void update(){
        UserEntity user = repository.findById(2);
        UserVO vo = new UserVO(user);
        vo.setUser_loginId("user1");
        vo.setPassword("1234");
        vo.setUser_name("정종민");
        vo.setUser_email("jjm92@gmail.com");
        vo.setUser_phone("010-9191-3131");
        vo.setNickname("팥종민");
        vo.setUser_birthToString("1992-08-17");
        vo.setUser_gender("남자");
        String[] roles = {"USER","HOST"};
        Role role = new Role(roles);
        vo.setRole(role);
        vo.setProfile_image_url("/bbangbbabg.png");
        PetInfo petInfo = new PetInfo("유월이","말티즈","14","6kg");
        vo.setPet_inform(petInfo);
        System.out.println("●".repeat(50));
        UserEntity userEntity = new UserEntity(vo);
        UserEntity result = repository.save(userEntity);
        System.out.println(result);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void updataPetInfo(){
        UserEntity entity = repository.findById(2);
        UserVO vo = new UserVO(entity);
        PetInfo info = new PetInfo("멍멍이","똥개","14","6kg");
        vo.setPet_inform(info);
        UserEntity userEntity = new UserEntity(vo);
        UserEntity result = repository.save(userEntity);
        System.out.println(result);
    }
}
