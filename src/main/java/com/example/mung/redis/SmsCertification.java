package com.example.mung.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertification {
    private final String PREFIX = "sms:"; // key값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 3 * 60; // 인증번호 유효 시간

    private final StringRedisTemplate stringRedisTemplate;

    // Redis에 저장
    public void createSmsCertification(String phone, String certificationNumber) {
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
        System.out.println("Stored in Redis1: " + certificationNumber);
    }

    // 휴대전화 번호에 해당하는 인증번호 불러오기
    public String getSmsCertification(String phone) {
        return stringRedisTemplate.opsForValue().get(PREFIX + phone);
    }

    // 인증 완료 시, 인증번호 Redis에서 삭제
    public void deleteSmsCertification(String phone) {
        stringRedisTemplate.delete(PREFIX + phone);
    }

    // Redis에 해당 휴대번호로 저장된 인증번호가 존재하는지 확인
    public boolean hasKey(String phone) {
        return stringRedisTemplate.hasKey(PREFIX + phone);
    }

    // Redis에서 인증번호가 존재하는지 확인하는 메서드
    public boolean isCertificationNumberValid(String phone, String certificationNumber) {
        // Redis에서 인증번호 키가 존재하는지 확인
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(PREFIX + phone))) {
            return false;  // 인증번호가 없으면 인증 실패
        }

        // 인증번호 값 가져오기
        String storedCertificationNumber = stringRedisTemplate.opsForValue().get(PREFIX + phone);

        // 입력된 인증번호와 비교
        return storedCertificationNumber != null && storedCertificationNumber.equals(certificationNumber);
    }



}