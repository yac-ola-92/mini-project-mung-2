package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accommodation")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accomId;

    @Column(nullable = false)
    private String accomName;

    private String accomImagesUrl;
}
