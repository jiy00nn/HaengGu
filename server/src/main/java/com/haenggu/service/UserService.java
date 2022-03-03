package com.haenggu.service;

import com.haenggu.auth.AccessTokenSocialTypeToken;
import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.RoleType;
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

    public Users updateUser(UUID id, Users data) {
        Users user = userRepository.getByUserId(id);
        if(data.getBirthday() != null) {
            user.setBirthday(data.getBirthday());
        }
        if(data.getSchool() != null) {
            user.setSchool(data.getSchool());
        }
        if(data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }
        if(data.getGender() != null) {
            user.setGender(data.getGender());
        }
        if(data.getGrade() != null) {
            user.setGrade(data.getGrade());
        }
        if(data.getUsername() != null) {
            user.setUsername(data.getUsername());
        }
        if(data.getEventTag() != null) {
            user.setEventTag(data.getEventTag());
        }
        if(data.getRegionTag() != null) {
            user.setRegionTag(data.getRegionTag());
        }
        user.setRoleType(RoleType.USER);
        return userRepository.save(user);
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
