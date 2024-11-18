package com.example.mung.domain;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomDTO {
    private int room_id;
    private int accom_id;
    private String room_name;
    private String room_type;
    private int room_price;
    private String room_images_url;
    private String room_info;
    private int room_amount;
    private String pet_kind;
    private int capacity_standard;
    private int capacity_max;


    public List<String> getRoomImagesUrl() {
        return Arrays.asList(getRoom_images_url().split(","));
    }

    public RoomDTO (int room_id, int accom_id, String room_images_url){
        this.room_id = room_id;
        this.accom_id = accom_id;
        this.room_images_url = room_images_url;
    }

    public RoomDTO(int room_id, int accom_id, String room_name, String room_type, int room_price,
            String room_info, int room_amount, String pet_kind, int capacity_standard, int capacity_max, String room_images_url  ){
        this.room_id = room_id;
        this.accom_id = accom_id;
        this.room_name = room_name;
        this.room_type = room_type;
        this.room_price =room_price;
        this.room_info = room_info;
        this.room_amount =room_amount;
        this.pet_kind =pet_kind;
        this.capacity_standard = capacity_standard;
        this.capacity_max = capacity_max;
        this.room_images_url=room_images_url;
    }


}
