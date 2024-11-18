package com.example.mung.repository;

import com.example.mung.domain.RoomDTO;
import com.example.mung.entity.Room;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {


    // 숙소id로 해당 숙소의 객실 전부 출력
    @Query("SELECT r FROM Room r WHERE r.accommodation.accom_id = :accom_id")
    public List<Room>findByAccomId(@Param("accom_id") int accom_id) ;

        //객실 상세에 보일 객실 이미지
    @Query("SELECT new com.example.mung.domain.RoomDTO(r.room_id, r.accommodation.accom_id, r.room_images_url)" +
            "  FROM Room r WHERE r.room_id = :room_id")
    public  RoomDTO findImgUrl(@Param("room_id") int room_id);

                // 객실 상세 이미지 제외
    @Query("SELECT new com.example.mung.domain.RoomDTO( r.room_id, r.accommodation.accom_id, r.room_name, r.room_type, r.room_price, " +
            " r.room_info, r.room_amount, r.pet_kind, r.capacity_standard, r.capacity_max,r.room_images_url) " +
            "FROM Room r WHERE r.room_id = :room_id")
    public RoomDTO findOne(@Param("room_id") int room_id);

}
