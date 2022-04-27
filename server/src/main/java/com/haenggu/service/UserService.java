package com.haenggu.service;

import com.haenggu.auth.AccessTokenSocialTypeToken;
import com.haenggu.domain.entity.EventLike;
import com.haenggu.domain.entity.UserImage;
import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.RoleType;
import com.haenggu.exception.FileStorageException;
import com.haenggu.http.response.event.EventBasicResponse;
import com.haenggu.http.response.UserResponse;
import com.haenggu.repository.UserImageRepository;
import com.haenggu.repository.UserRepository;
import com.haenggu.service.strategy.KakaoLoadStrategy;
import com.haenggu.auth.OAuth2UserDetails;
import com.haenggu.service.strategy.SocialLoadStrategy;
import com.haenggu.domain.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public UserRepository userRepository;
    public UserImageRepository userImageRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserImageRepository userImageRepository) {
        this.userRepository = userRepository;
        this.userImageRepository = userImageRepository;
    }

    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.getById(UUID.fromString(authentication.getPrincipal().toString()));
        List<EventBasicResponse> eventBasicResponse = new ArrayList<>();

        for(EventLike eventLike : user.getEventLikes()) {
            eventBasicResponse.add(new EventBasicResponse(eventLike.getEvent()));
        }

        String description = "";
        if(!ObjectUtils.isEmpty(user.getDescription())) {
            description = user.getDescription();
        }

        return UserResponse.builder()
                .imageId(user.getImage().getImageId())
                .username(user.getUsername())
                .description(description)
                .tags(user.getUserTags())
                .boards(user.getBoards())
                .events(eventBasicResponse)
                .build();
    }

    public Users updateUser(UUID id, Users data) {
        Users user = userRepository.getById(id);
        if(data.getBirthday() != null) {
            user.setBirthday(data.getBirthday());
        }
        if(data.getSchool() != null) {
            user.setSchool(data.getSchool());
        }
        if(data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }
        if(data.getDescription() != null) {
            user.setDescription(data.getDescription());
        }
        if(data.getGender() != null) {
            user.setGender(data.getGender());
        }
        if(data.getGrade() != null) {
            user.setGrade(data.getGrade());
        }
        if(data.getMbti() != null) {
            user.setMbti(data.getMbti());
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

    public UserImage storeFile(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getPrincipal().toString());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            UserImage userImage =
                    UserImage.builder()
                            .user(userRepository.getById(userId))
                            .mimetype(file.getContentType())
                            .originalName(file.getOriginalFilename())
                            .size(file.getSize())
                            .data(file.getBytes())
                            .build();

            return userImageRepository.save(userImage);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public UserImage loadFileAsByte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getPrincipal().toString());
        return userImageRepository.findUserImageByUser(userRepository.getById(userId));
    }

    public UserImage loadFileAsByte(UUID id) {
        return userImageRepository.getById(id);
    }
}
