package com.example.mung.service;

import com.example.mung.domain.RoomDTO;
import com.example.mung.domain.RoomVO;
import com.example.mung.entity.Room;

import java.util.List;

public interface RoomService {

    List<Room>findAll();
    List<Room>readByAccom_id(int accom_id);
    RoomDTO readOne(int room_id);
    RoomDTO readUrl(int room_id);
    boolean register(Room room);
    boolean modify(Room room);
    boolean remove(int room_id);

}

