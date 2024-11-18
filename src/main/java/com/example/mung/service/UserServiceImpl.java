package com.example.mung.service;

import com.example.mung.domain.UserDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.entity.UserEntity;
import com.example.mung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository repository;

    @Override
    public List<UserVO> findAll() {
        System.out.println("Service단 : findAll 실행");
        List<UserEntity> list = repository.findAll();
        List<UserVO> vo= new ArrayList<>();
        for (UserEntity userEntity : list) {
            vo.add(new UserVO(userEntity));
        }
        return vo;
    }

    @Override
    public UserDTO read(int id) {
        System.out.println("Service단 : read 실행");
        UserVO vo = new UserVO(repository.findById(id));
        return new UserDTO(vo);
    }

    @Override
    public boolean register(UserVO vo) {
        System.out.println("Service단 : register 실행");
        System.out.println(vo);
        repository.save(new UserEntity(vo));
        return true;
    }

    @Override
    public boolean register_b(UserVO vo) {
        System.out.println(vo);
        repository.save(new UserEntity(vo));
        return true;
    }

    @Override
    public boolean modify(UserVO vo) {
        System.out.println("Service단 : modify 실행");
        System.out.println(vo);
        repository.save(new UserEntity(vo));
        return true;
    }

    @Override
    public boolean remove(int id) {
        System.out.println("Service단 : remove 실행");
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean modify_pet(UserVO vo) {
        System.out.println("Service단 : modify_pet 실행");
        System.out.println(vo);
        repository.save(new UserEntity(vo));
        return true;
    }
}
