package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
public class Review {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int review_id;

    private int rating;
    private String comment;

}
