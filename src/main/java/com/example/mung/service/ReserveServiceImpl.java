package com.example.mung.service;

import com.example.mung.entity.Reservation;
import com.example.mung.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReserveServiceImpl implements ReserveService {
    @Autowired
    private ReservationRepository repository;

    public boolean register(Reservation reservation) {
        Reservation result = repository.save(reservation);
        System.out.println("서비스단:" + result);
        return true;
    }

    public Reservation getReservation(int id) {

        Reservation result = repository.getReferenceById(id);
        System.out.println("서비스단:" + result);
        return result;
    }
}
