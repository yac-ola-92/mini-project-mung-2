package com.example.mung.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.util.List;


@ToString(exclude = "room")
@Setter
@Getter
@Entity
@Table(name = "accommodation")
public class Accommodation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int accom_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

 /*User 는 클래스명 -> 테이블에 매핑
    user 는 인스턴스 변수(필드) Review 테이블의 레코드 하나를 나타내는 객체로,
    Review 테이블에 대한 참조를 의미하고, 이 변수로 Review 테이블의 데이터를 접근할 수 있습니다.
    */


    // Accommodation에서 여러 개의 Review를 참조

    @Column(name = "accom_name")
    private String accom_name;

    @Column(name = "accom_location")
    private String accom_location;

    @Column(name = "accom_phone")
    private String accom_phone;


    @Column(name = "accom_caution", columnDefinition = "TEXT")
    private String accom_caution;

    @Column(name = "accom_description", columnDefinition = "TEXT")
    private String accom_description;

    @Column(name = "accom_images_url", columnDefinition = "TEXT")
    private String accom_images_url;

    @Column(name = "accom_amenities")
    private String accom_amenities;


    @OneToMany(mappedBy = "accommodation" ,fetch = FetchType.LAZY)
    private List<Room> room;
}
