package com.example.mung.service;

import com.example.mung.domain.AccomDTO;
import com.example.mung.domain.RoomDTO;
import com.example.mung.domain.RoomVO;
import com.example.mung.entity.Room;
import com.example.mung.mapper.RoomMapper;
import com.example.mung.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceimpl implements RoomService {
    @Autowired
   private RoomRepository repo;

    @Override
    public List<Room>findAll(){
        System.out.println("모든 숙소의 객실 출력!!!!");
        return repo.findAll();
    }

    @Override // 한 숙소에서 가지고 있는 객실 모두 출력
    public List<Room>readByAccom_id(int accom_id){
        System.out.println(accom_id+"번 숙소의 객실 모두 출력!!");
        return repo.findByAccomId(accom_id);
    }


    @Override
    public RoomDTO readUrl(int room_id){
        System.out.println("url 가져올게요");
        return repo.findImgUrl(room_id);
    }

    @Override
    public RoomDTO readOne(int room_id) {
        System.out.println(room_id+"객실 정보 불러옵니다...");
        return repo.findOne(room_id);
    }

    @Override  //숙소의 객실 등록
    public boolean register(Room room){
        try {
            repo.save(room);
            System.out.println(room.getRoom_name()+"의 객실 등록 성공!!");
            return true;
        }catch (Exception e){
            System.out.println("객실 등록 실패 :" + e.getMessage());
            return false;
        }
    }

    @Override //등록된 객실 수정
    public boolean modify(Room room){
        try {
            repo.save(room);
            System.out.println(room.getRoom_name()+"의 객실 수정 성공!!");
            return true;
        }catch (Exception e){
            System.out.println("객실 등록 실패 :" + e.getMessage());
            return false;
        }
    }

    @Override  // 객실 삭제  리포지터리 미완성
    public boolean remove(int room_id) {
        try{
            repo.deleteById(room_id);
            System.out.println(room_id+"의 객실이 삭제되었습니다.");
            return true;
        }catch (Exception e){
            System.out.println("객실 삭제 실패");
            return false;
        }
    }
}
