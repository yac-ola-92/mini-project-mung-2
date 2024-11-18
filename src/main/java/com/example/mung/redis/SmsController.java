package com.example.mung.redis;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {
    private final SmsCertification smsCertification;
    private final MessageService messageService;


    // 인증 문자 발송
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendSms(@RequestBody Map<String, String> requestBody) {
        try {
            String phone = requestBody.get("phone");

            if (phone == null || phone.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "전화번호가 필요합니다."));
            }

            String resultMessage = messageService.sendSMS(phone);

            // 결과를 JSON 형태로 반환
            return ResponseEntity.ok().body(Map.of("message", resultMessage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류 발생"));
        }
    }





    // 인증 번호 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCertification(@RequestBody Map<String, String> requestBody) {
        String phone = requestBody.get("phone");
        String certificationNumber = requestBody.get("certificationNumber");

        if (phone == null || certificationNumber == null) {
            return ResponseEntity.badRequest().body("전화번호와 인증번호를 입력해주세요.");
        }

        // 인증번호를 비교하는 로직 예시
        boolean isValid = smsCertification.isCertificationNumberValid(phone, certificationNumber);

        if (isValid) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 실패");
        }
    }

}


