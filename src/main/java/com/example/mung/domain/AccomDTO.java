package com.example.mung.domain;



import com.example.mung.entity.Accommodation;
import com.example.mung.repository.AccommodationRepository;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccomDTO {
    private int accom_id;
    private int user_id;
    private String accom_name;
    private String accom_location;
    private String accom_phone;
    private String accom_caution;
    private String accom_description;
    private String accom_images_url;
    private String accom_amenities;

    private int rating;
    private String pet_kind;
    private int room_price;
    private int capacity_standard;
    private int capacity_max;
    private String business_number;
    private String business_sns_url;
    private String nickname;
    private String comment;

    public List<String> getAccomImagesUrl(String accUrl){
        List ur = Arrays.asList(accUrl.split(","));
        System.out.println("숙소 이미지 출력 : "+ ur);
        return ur;
    }
    public List<String> getAccomAmenities(String amenity) {
        List amen = Arrays.asList(amenity.split(","));
        System.out.println("숙소 편의시설 출력 : " + amen);
        return amen;
    }
    public AccomDTO(int accom_id){
        this.accom_id = accom_id;
    }

    //repository 같은 숙소 등록 확인
    public AccomDTO(String accom_name , String accom_location){
        this.accom_name = accom_name;
        this.accom_location = accom_location;
    }


            //숙소 상세페이지용 2
    public AccomDTO(int user_id, int accom_id, String accom_name ,String accom_location, String accom_phone, String accom_caution, String accom_description ,
             String accom_images_url, String accom_amenities, String business_number, String business_sns_url, String nickname){
        this.user_id = user_id;
        this.accom_id = accom_id;
        this.accom_name = accom_name;
        this.accom_location = accom_location;
        this.accom_phone = accom_phone;
        this.accom_caution = accom_caution;
        this.accom_description = accom_description;
        this.accom_images_url = accom_images_url;
        this.accom_amenities = accom_amenities;
        this.business_number = business_number;
        this.business_sns_url = business_sns_url;
        this.nickname = nickname;
    }

                //등록한 숙소 수정시 가져올 정보
    public AccomDTO(int accom_id, String accom_name ,String accom_location, String accom_phone, String accom_caution,
                    String accom_description, String accom_images_url, String accom_amenities){
        this.accom_id = accom_id;
        this.accom_name = accom_name;
        this.accom_location = accom_location;
        this.accom_phone = accom_phone;
        this.accom_caution = accom_caution;
        this.accom_description = accom_description;
        this.accom_images_url = accom_images_url;
        this.accom_amenities = accom_amenities;
    }

            // 등록한 숙소 불러오기 ( 호스트용 )
    public AccomDTO(int user_id, int accom_id, String accom_name, String accom_images_url){
        this.user_id =user_id;
        this.accom_id = accom_id;
        this.accom_name = accom_name;
        this.accom_images_url = accom_images_url;
    }

    public AccomDTO (Accommodation accom){
        this.user_id = accom.getUser().getUser_id();
        this.accom_id = accom.getAccom_id();
        this.accom_name = accom.getAccom_name();
        this.accom_location = accom.getAccom_location();
        this.accom_phone = accom.getAccom_phone();
        this.accom_caution = accom.getAccom_caution();
        this.accom_description = accom.getAccom_description();
        this.accom_images_url = accom.getAccom_images_url();
        this.accom_amenities = accom.getAccom_amenities();
    }

}
