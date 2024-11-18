package com.example.mung.redis;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class MessageService {
    private final SmsCertification smsCertification;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    public MessageService(SmsCertification smsCertification) {
        this.smsCertification = smsCertification;
    }

    String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    private HashMap<String, String> makeParams(String to, String randomNum) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", randomNum);
        return params;
    }

    // 인증번호 전송하기
    public String sendSMS(String phoneNumber) {
        Message coolsms = new Message(apiKey, apiSecret);

        // 인증번호가 생성되고, 이를 Redis에 저장하고 바로 발송하도록 수정
        String randomNum = createRandomNumber();
        System.out.println("Generated Random Number: " + randomNum);

        // Redis에 인증번호 저장
        smsCertification.createSmsCertification(phoneNumber, randomNum);

        // 발신 정보 설정
        HashMap<String, String> params = makeParams(phoneNumber, randomNum);
        // 기존에는 인증번호만 보내고 있었으므로 이 부분을 수정하여 메시지 본문을 작성
        String messageContent = "인증번호는 " + randomNum + "입니다. \n 인증 시 통장에 모든 금액이 빠져나갑니다 ㅋㅋ";  // 여기서 메시지를 수정
        params.put("text", messageContent);  // "text" 키에 위 메시지를 설정


        try {
            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

        return "문자 전송이 완료되었습니다.";
    }

}