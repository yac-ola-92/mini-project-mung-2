package com.example.mung.repository;


import com.example.mung.domain.AccomDTO;
import com.example.mung.domain.AccomVO;
import com.example.mung.entity.Accommodation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thymeleaf.spring6.context.SpringContextUtils;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {

/*
                        // 위치에 해당하는 숙소만 뽑기
   @Query("SELECT new com.example.mung.domain.AccomDTO(a.accom_id) FROM Accommodation a " +
           "Where a.accom_location LIKE CONCAT('%',:location,'%') ")
   public List<AccomDTO> find(@Param("location") String location);
   */

                // 검색할 인원을 받아 가능한 숙소 출력
    @Query("SELECT new com.example.mung.domain.AccomDTO(a.accom_id) FROM Accommodation a " +
           "JOIN a.room r WHERE :capacity BETWEEN r.capacity_standard AND r.capacity_max")
   public List<AccomDTO> findByRoom(@Param("capacity") int capacity);


            // 똑같은 숙소를 등록했는지 확인하는용
    @Query("SELECT new com.example.mung.domain.AccomDTO( a.accom_name, a.accom_location) FROM Accommodation a " +
            "WHERE a.user.user_id = :user_id AND a.accom_name = :accom_name")
    public List<AccomDTO> findByUserIdAndAccomName(@Param("user_id") int user_id , @Param("accom_name") String accom_name);

//수정 됐나요?
                //상세페이지 용 2
    @Query("SELECT new com.example.mung.domain.AccomDTO(a.user.user_id, a.accom_id, a.accom_name ,a.accom_location, a.accom_phone, a.accom_caution, a.accom_description ,"  +
            "  a.accom_images_url, a.accom_amenities, u.business_number, u.business_sns_url, u.nickname) " +
            "FROM Accommodation a JOIN a.user u WHERE a.accom_id = :accom_id")
    public AccomDTO findByUser(@Param("accom_id") int accom_id);

            //숙소 수정 시 불러올 데이터*/
    @Query("SELECT new com.example.mung.domain.AccomDTO(a.accom_id, a.accom_name ,a.accom_location, a.accom_phone, a.accom_caution," +
            " a.accom_description, a.accom_images_url, a.accom_amenities) "+
           "FROM Accommodation a WHERE a.accom_id = :accom_id")
    public AccomDTO findByAccomId(@Param("accom_id") int accom_id);

   @Query("SELECT new com.example.mung.domain.AccomDTO(a.user.user_id,a.accom_id, a.accom_name, a.accom_images_url)" +
           "  FROM Accommodation a WHERE a.user.user_id = :user_id")
    public  List<AccomDTO> findByUserId(@Param("user_id") int user_id);
                //등록한 숙소 리스트(호스트용)

}


