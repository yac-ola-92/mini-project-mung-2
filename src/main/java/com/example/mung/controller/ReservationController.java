package com.example.mung.controller;

import com.example.mung.entity.Reservation;
import com.example.mung.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReserveService service;

    @Autowired
    public ReservationController(ReserveService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Reservation>getOne(int id) {
        Reservation reservation=service.getReservation(id);
        return ResponseEntity.ok().body(reservation);
    }

    @PostMapping
    public ResponseEntity<Boolean>postOne(Reservation reservation) {
        boolean result = service.register(reservation);
        return ResponseEntity.ok(result);
    }
}
