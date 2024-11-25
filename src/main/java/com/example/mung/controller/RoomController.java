package com.example.mung.controller;

import com.example.mung.domain.AccomDTO;
import com.example.mung.domain.RoomDTO;
import com.example.mung.domain.RoomVO;
import com.example.mung.entity.Room;
import com.example.mung.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomController {
    @Autowired
    private RoomService service;


    @PostMapping("/room_register")
    public String room_registration( Room room ){
        service.register(room);

        return "myAccomList";
    }

    @ResponseBody // json 방식으로 변환해서 보내기 위함
    @GetMapping("/room/{room_id}") //객실 모달에 보낼 값들
    public RoomDTO roomList(@PathVariable("room_id")int room_id){
        RoomDTO dto =  service.readOne(room_id);

        return dto ;
    }

}

