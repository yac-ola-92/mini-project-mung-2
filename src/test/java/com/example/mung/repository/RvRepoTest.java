//package com.example.mung.repository;
//
//import com.example.mung.entity.Reservation;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//public class RvRepoTest {
//    @Autowired
//    private ReservationRepository repository;
//
//    @BeforeEach
//    void pr() {
//        System.out.println("■".repeat(50));
//    }
//    @AfterEach
//    void pr1() {
//        System.out.println("■".repeat(50));
//    }
//
//    @Test
//    public void findAll() {
//        List<Reservation>list = repository.findAll();
//       list.stream().forEach(System.out::println);
//       List<String> data = new ArrayList<>();
//        list.forEach(reservation -> if(reservation.getUser_id()=='1'){
//            data.add(reservation)
//        });
//        System.out.println( list.get(0).getUser_id().getPet_info());
//    }
//
//    @Test
//    public void findById() {
//        Optional<Reservation> optional = repository.findById(1);
//        System.out.println(optional);
//    }
//}
