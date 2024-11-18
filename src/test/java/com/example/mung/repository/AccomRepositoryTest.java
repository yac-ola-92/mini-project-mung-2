package com.example.mung.repository;

import com.example.mung.domain.AccomDTO;
import com.example.mung.entity.Accommodation;
import com.example.mung.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AccomRepositoryTest {

    @Autowired
    private AccommodationRepository accomRp;

    @BeforeEach
    void pr(){
        System.out.println("**********************************************");
    }



   @Test
   void readByRoom(){
        List<AccomDTO> list = accomRp.findByRoom(3);
        if (list.isEmpty()) {
            System.out.println("값이 출력되고 있지 않습니다");
        }else {
            list.stream().forEach(System.out::println);
        }
   }

   @Test
    void readByUser(){
       System.out.println(accomRp.findByUser(47));
   }

   @Test
    void readByAccomId(){
       System.out.println(accomRp.findByAccomId(47));
   }

   @Test
   void readByUserId(){
        List<AccomDTO> list = accomRp.findByUserId(6);
       System.out.println("리스트 사이즈 : " + list.size());
       list.stream().forEach(System.out::println);

   }

    @Transactional
    @Test
    void register(){
        Accommodation accom = new Accommodation();
        UserEntity user = new UserEntity();
        user.setUser_id(3); //user_id 가 외래키라서 set할 때 user 객체를 따로 만들어서 설정
        accom.setUser(user);
        accom.setAccom_name("뭉개뭉개인강릉");
        accom.setAccom_location("강원특별자치도 강릉시 구정면 금평로 140");
        accom.setAccom_phone("010-2962-9240");
        accom.setAccom_caution("[인원 추가정보]  \n" +
                "2인1견 기준, 최대4인 4견\\n기준인원 초과 1인 3만원 1견 1만원\\n\\n[시설정보]  \n" +
                "방1 : 더블침대(Q), 펫 포토존, 빌트인 에어컨, 소화기, 손전등, 펫 침대, 펫 계단, 옷걸이행거\\n방2 : 싱글침대(SS), 빌트인 에어컨, 소화기, 손전등, 옷걸이행거\\n주방 : 식탁, 조리도구, 직수냉온정수기, 냉장고, 전자레인지, 커피머신, 커피포트, 인덕션, 자동와인오프너, 에어프라이어\\n거실 : 쇼파, TV, 무선청소기, 빌트인 냉난방기, 실링팬, 패드, 매너벨트, 탈취제, 버그 스프레이, 펫 식기, 펫 침대, wifi\\n욕실 : 타월, 드라이기, 샴푸, 린스, 바디클린저, 샤워타월, 손세정제, 비데, 클렌징 폼, 펌핑치약\\n펫 욕실 : 펫 스파 욕조, 펫 스탠드 드라이기, 펫 미용 테이블, 귀세정패드, 펫 타월, 하이포닉 샴푸(린스겸용), 펫 하이포닉 미스트, 펫 슬리커 브러쉬, 펫 페이스 콤 브러쉬\\n야외 : 인조잔디 운동장, 배변봉투, 분리수거 쓰레기통, 운동장 가로등(일몰~23시), 야외테이블세트, 바베큐(가스버너), 어질리티, 펫 수영장(7~8월만 운영)\\n\\n[입,퇴실 안내]  \n" +
                "입실 오후 3시\\n퇴실 오전 11시(퇴실 시간 엄수)\\n\\n[픽업안내]  \n" +
                "픽업불가\\n\\n예약된 인원 이외 방문불가\\n펜션 실내 실외 절대 금연(주차장에서 흡연가능)\\n바베큐(가스버너) 이용은 실외 지정된 장소에서만 가능, 실내 간단한 취사는 가능하나 냄새가 심한 음식(육류, 생선등)은 조리 금지\\n보호자 없는 미성년자 예약불가\\n운동장 이용시 배변은 배변봉투에 담아 휴지통에 버려주세요\\n안전과 방범을 위해 건물 외부에 CCTV가 설치되어 있으나 문단속을 철저히 해주세요. 모든 물건에 대해 분실 시 책임지지 않습니다.\\n반려동물을 숙소 내, 마당에 두고 외출 하는것은 절대 불가합니다.\\n실내마킹, 대소변 못가리는 반려견은 매너벨트 착용필수(실수시 바로 치워주세요)\\n기물파손, 침구류 오염등이 발생한경우 퇴실전 꼭 알려주세요. 파손이나 세탁이 안될 경우 손해배상청구됩니다.\\n숙소 내, 외부, 담장 안, 밖에서 일어나는 사건, 사고에 대해서는 책임을 지지않습니다. 보호자님의 각별한 주의 요청드립니다.\\n자연과 인접한 공간이라 곤충이나 벌레등이 실내로 유입될수 있으니 양지하시기 바랍니다.\n");
        accom.setAccom_description("한팀만을 위한 독채애견펜션\n" +
                "댕댕이친구들이 맘껏 뛰어놀수 있는 100평의 넓은 마당과 자연이 주는 고즈넉한 편안함~\n");
        accom.setAccom_images_url("https://files.ban-life.com/stay/20/11/2023/6cf48df873ba4419990181f7586ac5ac.jpeg,https://files.ban-life.com/stay/20/11/2023/577c775faeeb484aa8a810fd571d45fc.jpeg,https://files.ban-life.com/stay/20/11/2023/28426bf47b774a4a991fe0a71220863a.jpeg,https://files.ban-life.com/stay/20/11/2023/05434fbf71a741578f8c92ed6a1da34c.jpeg");
        accom.setAccom_amenities("~15kg,중형견,드라이룸,셀프목욕,애견수영장,애견운동장,포토존,애견샤워장,무선인터넷,취사가능,바베큐장,주차가능,CCTV");
        accomRp.save(accom);


    }

   @Transactional
   @Test
    void update(){
       Accommodation accom = new Accommodation();
       UserEntity user = new UserEntity();
       accom.setAccom_id(1);
       user.setUser_id(1); //user_id 가 외래키라서 set할 때 user 객체를 따로 만들어서 설정
       accom.setUser(user);
       accom.setAccom_name("가평 유명산고모네애견동반캠핑장");
       accom.setAccom_location("경기 가평군 설악면 어비산길 201-41");
       accom.setAccom_phone("070-4630-6450");
       accom.setAccom_caution("UPDATE 숙소테이블\n" +
               "SET accom_caution = '개별바비큐\\n- 종류: 장작 1회 구매시 화롯대제공(숯, 석쇠 불판 별도 구매)\\n  장작10Kg 1만원(현장 결제)\\n- 가능객실: 캠핑카, 편백1,2,3\\n- 이용요금: 15,000원\\n- 신청시간: 제한없음\\n- 이용시간: 제한없음\\n※ 동계 및 우천 시 이용 가능(사계절)\\n\\nIPTV/WiFi\\n\\n커플룸\\n\\n객실금연\\n\\n글램핑\\n\\n매점/편의점\\n- 운영 시간 : 입실 후 ~ 22시\\n- 판매 품목 : 1회용품, 물, 라면, 컵라면, 음료, 가스, 석쇠 불판, 장작, 과자 등등\\n불멍\\n- 이용 요금 : 화롯대+장작(10kg)+오로라가루 20,000원\\n- 이용 시간 : 입실 후 ~ 24시\\n- 이용 장소 : 객실 앞 또는 1층 파쇄석에서 진행 가능 \\n\\n반려동물\\n- 이용요금 : 1마리 당 1박 기준 10,000원 (현장결제)\\n- 이용객실 : 전 객실\\n- 제한무게 : 7KG\\n- 제한마릿수 : 2마리\\n- 애견비품 미제공\\n\\n주차가능\\n- 1대가능\\n- 차량 추가 시 1대당 – 1만원 (1박기준)\\n\\n패밀리\\n\\n계곡인접\\n\\n펜션즐기기\\n[부대시설]\\n- 공용화장실\\n- 공용샤워장\\n- 공용개수대\\n\\n* 현장에서 발생되는 모든 추가 요금은 1박당 부과되오니 자세한 사항은 숙소 측에 문의 부탁드립니다\\n* 숙박업소는 법적으로 청소년 혼숙이 금지되어 있습니다. 또한 미성년자의 예약 및 이용은 보호자 미 동반 시 숙박이 불가하며 보호자 동반 없이 현장 방문 시 환불받을 수 없으며 퇴실 조치됩니다.\\n* 펜션의 객실 예약 현황은 실시간 예약 특성상 100% 일치하지 않을 수 있습니다.\\n* 경우에 따라 1객실에 중복 예약이 발생할 수 있으며, 이 경우 먼저 결제된 예약 건에 우선권이 있습니다.\\n* 펜션의 객실 가격은 기간에 따라 상이할 수 있습니다.\\n* 취소수수료는 결제금액이 아닌 예약 금액(객실 금액 옵션 금액) 기준으로 책정됩니다.\\n* 최대 인원 초과 시 입실이 불가할 수 있으며, 해당 사유로 환불을 받으실 수 없습니다.(유아, 어린이 포함)\\n* 이용일 당일 예약 후 당일 취소한 경우 이용 당일 취소이므로 환불이 불가합니다.\\n* 취소수수료는 판매채널별로 상이할 수 있습니다.\\n* 당일예약. 당일 입실의 경우(특히 18시 이후 늦은 입실) 이용이 불가할 수도 있으니 반드시 이용 가능 여부를 펜션 측에 문의 후 구매해 주시기 바랍니다. Pm21시 이후 예약 건들은 100% 확정예약을 보장하지 않습니다.\\n\\n22시 이후 입실하실 경우 펜션으로 미리 연락 부탁드립니다\\n[펜션 공지 사항]\\nㆍ차량 추가 요금(현장 결제)\\n- 차량 추가 시 1대당 – 1만원 (1박기준)\\nㆍ퇴실 시간 (11시) 초과 시 (현장결제)\\n- 시간 당 5,000원\\nㆍ캠핑용 장작 판매(현장 결제)\\n- 10Kg – 1만원\\nㆍ이불 추가 요금(현장 결제)\\n- 1만원 (인원 추가 비용과 별도임)\\nㆍ소주 / 맥주 / 부탄가스 = 3,000원\\nㆍ시설물, 가구 및 구비물품 파손 또는 오염시 부득이 배상 청구되오니 주의바랍니다.\\n* 객실 내 취사를 금지합니다.'\n" +
               "WHERE id = 1;\n");
       accom.setAccom_description("안녕하세요. 가평 유명산고모네 애견동반 캠핑장입니다.\n");
       accom.setAccom_images_url("https://files.ban-life.com/accommodation/2024/07/304db6e7f4-4f63-4aa3-8623-12ca7c28b62f.jpg,https://files.ban-life.com/accommodation/2024/07/304be9ac3b-11ff-4e15-8008-ddfaed005d5d.jpg,https://files.ban-life.com/accommodation/2024/07/30b651f66f-878f-49a6-b555-2384d173cd31.jpg,https://files.ban-life.com/accommodation/2024/07/30e87176a5-dc39-434a-a92a-78c82e4c47f5.jpg");
       accom.setAccom_amenities("~7kg,소형견,무선인터넷,매점,금연,주차가능");

       accomRp.save(accom);
       System.out.println(accomRp.findByAccomId(1));
       }

       @Transactional
       @Test
    void delete(){
        accomRp.deleteById(8);
           System.out.println("삭제 성공");

       }
    }


