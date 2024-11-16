package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
public class Room {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int room_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accom_id")
    private Accommodation accommodation;

    private String room_name;
    private String room_type;
    private int room_price;
    @Column(name = "room_images_url",columnDefinition = "TEXT")
    private String room_images_url;
    private String room_info;
    private int room_amount;
    private String pet_kind;
    private int capacity_standard;
    private int capacity_max;

}
