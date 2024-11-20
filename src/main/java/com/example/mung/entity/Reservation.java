package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rv_id;

    // Room과 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id",nullable = false, insertable = false, updatable = false)
    private Room room_id;

    // User와 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id" ,insertable = false,updatable = false)
    private UserEntity user_id;

    @Column(name = "rv_start_date" ,nullable = false)
    private LocalDateTime rv_start_date;

    @Column(name = "rv_end_date" ,nullable = false)
    private LocalDateTime rv_end_date;

    @Column(name = "guest_count",nullable = false)
    private int guest_count;

    @Column(name = "total_price",nullable = false)
    private int total_price;




}
