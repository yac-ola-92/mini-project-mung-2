package com.example.mung.repository;


import com.example.mung.entity.Accommodation;
import com.example.mung.entity.Room;
import com.example.mung.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.AbstractCookieValueMethodArgumentResolver;

import java.util.List;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRp;

/*    @Autowired
    ReservationRepository rev;  객실 삭제  */

    @BeforeEach
    void pre(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @Test
    void findAll(){
        List<Room>list = roomRp.findAll();
        list.stream().forEach(System.out::println);

    }

    @Transactional
    @Test
    void getAccomRoom(){
      List<Room> list = roomRp.findByAccomId(1);
      list.stream().forEach(System.out::println);
    }

    @Test
    void getUrl(){
        System.out.println( roomRp.findImgUrl(1));
    }

    @Test
    void getOne(){
        System.out.println(roomRp.findOne(1));
    }

    @Transactional
    @Test
    void register(){
        Room room = new Room();
        Accommodation accom  = new Accommodation();
        accom.setAccom_id(1);
        room.setAccommodation(accom);
        room.setRoom_name("편백4");
        room.setRoom_type("캠핑");
        room.setRoom_price(69000);
        room.setRoom_images_url("https://files.ban-life.com/room/2024/07/3060d94dce-c51e-4e54-998e-26e32b8fb8c9.jpg,https://files.ban-life.com/room/2024/07/301188d14c-d15a-4bfc-b30f-fe76db2750cc.jpg");
        room.setRoom_info("기준 2인 / 최대 4인 2마리\n" +
                "원룸 · 독채 · 온돌 · 커플 · 가족\n" +
                "체크인 15:00 · 체크아웃 11:00");
        room.setRoom_amount(4);
        room.setPet_kind("대형견");
        room.setCapacity_standard(2);
        room.setCapacity_max(4);
        roomRp.save(room);
        roomRp.findByAccomId(1).stream().forEach(System.out::println);
    }

    @Test
    void update() {
        Room room = new Room();
        Accommodation accom  = new Accommodation();
        room.setRoom_id(1);
        accom.setAccom_id(1);
        room.setAccommodation(accom);
        room.setRoom_name("캠핑카");
        room.setRoom_type("캠핑");
        room.setRoom_price(109000);
        room.setRoom_images_url("https://files.ban-life.com/room/2024/07/303ae9cc7a-d471-43c4-80e4-149821afd4f0.https://files.ban-life.com/room/2024/07/300fdea259-6fc7-4564-8550-54d0cc536f8e.jpg");
        room.setRoom_info("기준 2인 / 최대 4인 2마리\n" +
                "침대 · 커플 · 가족 · 카라반\n" +
                "체크인 15:00 · 체크아웃 11:00");
        room.setRoom_amount(5);
        room.setPet_kind("대형견");
        room.setCapacity_standard(2);
        room.setCapacity_max(4);
        roomRp.save(room);
        roomRp.findByAccomId(1).stream().forEach(System.out::println);
    }

    @Transactional
    @Test
    void Delete(){
        //이건 예약 리포지터리 만들어지면 그때 예약 있는지 확인하고 삭제 구현할 수 있음

        /*
        boolean rv = rev.deleteById(rv_id);

         if(rv){
            roomRp.deleteById(1);
        System.out.println("삭제 성공");
         }else{
         System.out.println("해당 룸을 예약한 고객이 있습니다.");
         }

        * */
        roomRp.deleteById(1);
        roomRp.findByAccomId(1).stream().forEach(System.out::println);
    }


}
