package com.example.mung.controller;

import com.example.mung.domain.AccomDTO;
import com.example.mung.domain.RoomDTO;
import com.example.mung.domain.UserVO;
import com.example.mung.entity.Accommodation;
import com.example.mung.entity.Room;
import com.example.mung.service.AccomService;
import com.example.mung.service.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AccomRestController {

    @Autowired
    private AccomService service;

    @Autowired
    private RoomService rService;


    @GetMapping("/getId")
    public ResponseEntity<Integer> getSession(HttpSession session){
        UserVO info = (UserVO) session.getAttribute("userInfo");

        Integer id = info !=null ?  info.getUser_id() : null;

        return  ResponseEntity.ok(4);
    }

    @GetMapping("/myAccomList/{user_id}")
    public ResponseEntity<List<AccomDTO>> myAccomList(@PathVariable int user_id){
        System.out.println(user_id);
        try {
            List<AccomDTO> myAccom = service.findByUserId(user_id);
            System.out.println(myAccom+"myAccomList");
            if (myAccom.isEmpty()) {
                myAccom = new ArrayList<>();
            }
            return ResponseEntity.ok(myAccom);
        }
        catch (NumberFormatException e){

        }
        return ResponseEntity.badRequest().body(new ArrayList<>());
    }


    @GetMapping("/myAccom/edit/{accom_id}") //수정할 숙소 불러오기
//url 요청 접수
    public ResponseEntity<AccomDTO> accom_edit(@PathVariable int accom_id){ //id값을 매개변수로 받음
        System.out.println("받아온 숙소 아이디: "+accom_id);
        //수정할 데이터들을 받아옴
        AccomDTO acc=new AccomDTO();
        try{
             acc = service.readByAccomId(accom_id);
            System.out.println("해당 숙소를 찾아옵니다 Con :"+acc);
            if(acc ==null){    // 해당 아이디의 숙소가 존재하지 않을 때
                System.out.println("해당 숙소를 찾을 수 없습니다");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 수정된 부분
            }
            return  ResponseEntity.ok(acc);
        }catch (Exception e){

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    @PostMapping("/accom_update") //숙소 수정
    public String accom_update(@RequestBody Accommodation accom){
        service.modify(accom);
        return "update_accom "; // 마이페이지의 숙소리스트로 돌아갈거임
    }


    @GetMapping("/myRoom/{accom_id}")
    public ResponseEntity<List<RoomDTO>> room_edit(@PathVariable("accom_id") int accom_id){
        System.out.println(accom_id + " 숙소 수정을 위해 객실정보를 찾겠습니다. controller");
        try {
            List<RoomDTO> rm = rService.readByAccom_id(accom_id);
            System.out.println("수정할 숙소의 객실 " + rm);
            return ResponseEntity.ok(rm);  // 객실 정보 반환
        } catch (Exception e) {
            System.out.println("객실을 가져오는데 오류가 발생했습니다.");
            e.printStackTrace();  // 예외 로그 출력
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // 예외 처리
    }


    @PostMapping("/room_update")
    public String room_update(Room room){
        rService.modify(room);
        return "myAccomList";
    }

    @DeleteMapping("/accom_delete/{accom_id}") //숙소 삭제
    public ResponseEntity<String>  accom_delete(@PathVariable int accom_id){
        System.out.println("삭제하려는 숙소 아이디 : " + accom_id);
        try {
            service.remove(accom_id);
            return ResponseEntity.ok("숙소를 삭제했습니다");
        }catch (Exception e){

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("숙소 삭제 실패");
    }


    @PostMapping("/room_delete/{room_id}")
    public String room_delete(@PathVariable int room_id){
        service.remove(room_id);
        return "redirect:다시 리스트 페이지로 이동";
    }

}
