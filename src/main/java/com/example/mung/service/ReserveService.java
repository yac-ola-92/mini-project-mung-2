package com.example.mung.service;

import com.example.mung.entity.Reservation;

public interface ReserveService {

    public boolean register(Reservation record);
    public Reservation getReservation(int id);

}
