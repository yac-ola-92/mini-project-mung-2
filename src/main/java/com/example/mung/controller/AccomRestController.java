package com.example.mung.controller;

import com.example.mung.domain.AccomDTO;
import com.example.mung.entity.Accommodation;
import com.example.mung.entity.Room;
import com.example.mung.service.AccomService;
import com.example.mung.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class AccomRestController {

    @Autowired
    private AccomService service;

    @Autowired
    private RoomService rService;

    @GetMapping("/myAccomList/{user_id}")
    public List<AccomDTO> myAccomList(int user_id){
        List<AccomDTO> myAccom = service.findByUserId(user_id);

        return myAccom;
    }


    @GetMapping("/myAccom/edit/{accom_id}") //수정할 숙소 불러오기
//url 요청 접수
    public String accom_edit(@PathVariable int accom_id, Model model){ //id값을 매개변수로 받음
        AccomDTO acc = service.readByAccomId(accom_id);
        //수정할 데이터들을 받아옴
        if(acc!=null){
            // 모델에 데이터 등록
            model.addAttribute("accInfo",acc);
        }else {
            return "redirect:/error/404";

        }
        return "update_accom";
    }


    @PostMapping("/accom_update") //숙소 수정
    public String accom_update(@RequestBody Accommodation accom){
        service.modify(accom);
        return "redirect:/myPage "; // 마이페이지의 숙소리스트로 돌아갈거임
    }


    @GetMapping("/myAccom/{accom_id}/edi") // 숙소 수정에 사용될 정보
    public String room_edit(@PathVariable("accom_id") int accom_id, Model model){
        List<Room> rm = rService.readByAccom_id(accom_id);
        model.addAttribute("rmInfo",rm);
        return "update_accom";
    }


    @PostMapping("/room_update")
    public String room_update(Room room){
        rService.modify(room);
        return "myAccomList";
    }

    @DeleteMapping("/accom_delete") //숙소 삭제
    public String  accom_delete(@RequestParam int accom_id){
        service.remove(accom_id);
        return "redirect:다시리스트페이지로";
    }


    @PostMapping("/room_delete")
    public String room_delete(@RequestParam int room_id){
        service.remove(room_id);
        return "redirect:다시 리스트 페이지로 이동";
    }

}
