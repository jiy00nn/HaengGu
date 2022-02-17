package com.haenggu.service.strategy;

import com.haenggu.domain.enums.SocialType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class KakaoLoadStrategy extends SocialLoadStrategy {
    protected String sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.KAKAO.getUserInfoUrl(),
                    SocialType.KAKAO.getMethod(),
                    request,
                    RESPONSE_TYPE);

            return response.getBody().get("id").toString();//카카오는 id를 PK로 사용

        } catch (Exception e) {
            System.out.println("AccessToken을 사용하여 KAKAO 유저정보를 받아오던 중 예외가 발생했습니다. " + e.getMessage());
            // log.error("AccessToken을 사용하여 KAKAO 유저정보를 받아오던 중 예외가 발생했습니다 {}" ,e.getMessage() );
            throw e;
        }
    }
}
