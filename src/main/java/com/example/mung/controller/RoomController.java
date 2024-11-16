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


    @GetMapping("/myAccom/{accom_id}/edi") // 숙소 수정에 사용될 정보
    public String room_edit(@PathVariable("accom_id") int accom_id, Model model){
        List<Room> rm = service.readByAccom_id(accom_id);
        model.addAttribute("rmInfo",rm);
        return "update_accom";
    }



    @PostMapping("/room_update")
    public String room_update(Room room){
        service.modify(room);
        return "myAccomList";
    }


    @GetMapping("/room/{accom_id}")
    public String room_list(@PathVariable("accom_id")int accom_id, Model model){
        System.out.println("roomController에서 받은 숙소 id : "+ accom_id);
        RoomDTO ru = service.readUrl(accom_id);
        List<Room> room_dto = service.readByAccom_id(accom_id);
        System.out.println("User와 쪼인 :"+ room_dto);

        if(room_dto !=null){
            model.addAttribute("roomUrl",ru.getRoomImagesUrl());
            model.addAttribute("room", room_dto);
        }else {
            return "/error/404";
        }
        return "/accomDetail"; // 상세 페이지 반환
    }


    @ResponseBody // json 방식으로 변환해서 보내기 위함
    @GetMapping("/room/{room_id}") //객실 모달에 보낼 값들
    public RoomDTO roomList(@PathVariable("room_id")int room_id){
        RoomDTO dto = service.readOne(room_id);
        return dto;
    }



    @PostMapping("/room_delete")
    public String room_delete(@RequestParam int room_id){
        service.remove(room_id);
        return "redirect:다시 리스트 페이지로 이동";
    }

}

