package com.example.mung.controller;

import com.example.mung.domain.AccomDTO;
import com.example.mung.entity.Accommodation;
import com.example.mung.service.AccomService;
import com.example.mung.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class AccomRestController {

    @Autowired
    private AccomService service;

    @Autowired
    private RoomService rService;

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


}
