package com.haenggu.service;

import com.haenggu.auth.AccessTokenSocialTypeToken;
import com.haenggu.domain.entity.Users;
import com.haenggu.repository.UserRepository;
import com.haenggu.service.strategy.KakaoLoadStrategy;
import com.haenggu.auth.OAuth2UserDetails;
import com.haenggu.service.strategy.SocialLoadStrategy;
import com.haenggu.domain.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    public UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getUser(UUID id) {
        return userRepository.getByUserId(id);
    }

    public OAuth2UserDetails getOAuth2UserDetails(AccessTokenSocialTypeToken authentication)  {
        SocialType socialType = authentication.getSocialType();

        SocialLoadStrategy socialLoadStrategy = getSocialLoadStrategy(socialType); //SocialLoadStrategy 설정
        String socialPk = socialLoadStrategy.getSocialPk(authentication.getAccessToken());//PK 가져오기

        return OAuth2UserDetails.builder() //PK와 SocialType을 통해 회원 생성
                .principal(socialPk)
                .socialType(socialType)
                .build();
    }

    private SocialLoadStrategy getSocialLoadStrategy(SocialType socialType) {
        if (socialType == SocialType.KAKAO) {
            return new KakaoLoadStrategy();
        } else {
            throw new IllegalArgumentException("지원하지 않는 로그인 형식입니다");
        }
    }
}
