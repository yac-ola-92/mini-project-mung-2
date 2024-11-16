package com.example.mung.service;

import com.example.mung.domain.AccomDTO;
import com.example.mung.domain.AccomVO;
import com.example.mung.entity.Accommodation;
import com.example.mung.entity.User;
import com.example.mung.mapper.AccomMapper;
import com.example.mung.repository.AccommodationRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 스프링에서 빈으로 인식하기위한 어노테이션
@Service
public class AccomServiceImpl implements AccomService {
    // AccomServiceImpl는 DAO 메소드 호출 구현
    @Autowired
    private AccommodationRepository repo; //AccomMapper 받아서 accomDAO 객체 생성

    @Autowired
    private AccomMapper mapper;

    @Override
    public List<AccomDTO> readByLocation(String location, int capacity) {  // 위치에 따른 숙소 출력
        System.out.println(location + " 지역에 있는 숙소를 출력합니다");
        return mapper.getListByLocation(location, capacity);

    }


    @Override
    public AccomDTO readByUser(int accom_id) {          // 상세 페이지용
        System.out.println("User 와 쪼인 성공!S");
        System.out.println(accom_id + "번에 해당하는 숙소 정보를 불러옵니다.");
          AccomDTO acc =repo.findByUser(accom_id);
        System.out.println("서비스단에서 출력:"+acc);
        return acc;
    }


    @Override
    public List<AccomDTO>readByReview(int accom_id){   // 상세 페이지용
        System.out.println("Review 와 쪼인 성공!S");
        System.out.println(accom_id + "번에 해당하는 숙소 정보를 불러옵니다");
        List<AccomDTO> rev =null;
        try{
           rev = mapper.getOneByReview(accom_id);
        } catch(Exception e){
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%");
            e.printStackTrace();
        }

        if (rev == null) {
            rev = new ArrayList<>(); //리뷰가 없을 수도 있기때문에
        }
        System.out.println("서비스단에서 출력하는 리뷰 :"+rev);

        return rev;
    }

    @Override
    public List<AccomDTO> readByRating() {   // 별점이 높은 순서로 숙소 출력
        System.out.println("별점 높은 숙소들이라구~");
        return mapper.getListByRating();
    }

   @Override
    public AccomDTO readByAccomId(int accom_id){   // 숙소 수정할때 불러올 데이터
        System.out.println("숙소 수정을 위한 숙소 데이터를 줍줍!");
        return repo.findByAccomId(accom_id);
    }

    @Override
    public  List<AccomDTO> findByUserId(int user_id){ // 호스트의 등록한 숙소 리스트
        System.out.println(user_id+"호스트의 등록된 숙소를 출력합니다 ");
        return repo.findByUserId(user_id);
    }


    @Transactional
    @Override
    public boolean register(Accommodation accom) { //숙소 등록

        List<AccomDTO> checkAccom = repo.findByUserIdAndAccomName(accom.getUser().getUser_id()
                                                                        ,accom.getAccom_location());

        System.out.println(checkAccom.isEmpty());
        if (!checkAccom.isEmpty()) {
            System.out.println("이미 존재해서 등록 안할거~!");
            throw new IllegalArgumentException("이미 존재하는 숙소");
            // 유저아이디와 숙소명으로 찾아낸 숙소를 checkAccom에 넣고 그 값이 존재하면
            // 존재하는 숙소임을 확인!
        } else {
            repo.save(accom);
            System.out.println(" 숙소 등록 성공");
            return true;
            //그렇지 않다면 숙소를 삽입하고 객체반환;
        }
    }

    @Transactional
    @Override
    public boolean modify(Accommodation accom) { //숙소 수정
        try {
            // 숙소 업데이트
            repo.save(accom);

            // 숙소 업데이트 성공
            System.out.println(repo.findByAccomId(accom.getAccom_id()));
            System.out.println("숙소 업데이트 성공");

            return true;
        } catch (Exception e) {

            System.out.println("숙소 업데이트 실패: " + e.getMessage());
            return false;
        }
    }

    @Transactional
   @Override
    public boolean remove(int accom_id) { //숙소 삭제

        System.out.println("숙소 삭제 성공 ");
        repo.deleteById(8);
        return true;
    }

}
