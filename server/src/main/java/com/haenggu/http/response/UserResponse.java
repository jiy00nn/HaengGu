package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.*;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private String username;
    private String email;
    private String principal;
    private SocialType socialType;
    private RoleType roleType;
    private GenderType gender;
    private LocalDateTime birthday;
    private SchoolResponse school;
    private Integer grade;
    private MbtiType mbti;
    private List<String> eventTag;
    private List<String> regionTag;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public UserResponse(Users user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.principal = user.getPrincipal();
        this.socialType = user.getSocialType();
        this.roleType = user.getRoleType();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.school = SchoolResponse.builder()
                .schoolName(user.getSchool().getSchoolName())
                .deptName(user.getSchool().getDeptName())
                .build();
        this.grade = user.getGrade();
        this.mbti = user.getMbti();
        this.eventTag = user.getEventTag().stream()
                .map(CategoryType::getValue)
                .collect(Collectors.toList());
        this.regionTag = user.getRegionTag().stream()
                .map(RegionType::getValue)
                .collect(Collectors.toList());
        this.createdDate = user.getCreatedDate();
        this.modifiedDate = user.getModifiedDate();
    }
}
