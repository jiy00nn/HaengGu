package com.haenggu.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.GenderType;
import com.haenggu.domain.enums.MbtiType;
import com.haenggu.domain.enums.RegionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
    private final String username;
    private final String email;
    private final String description;
    private final UUID deptId;
    private final GenderType gender;
    private final Integer grade;
    private final MbtiType mbti;
    private final LocalDate birthday;
    private final List<CategoryType> categoryTag;
    private final List<RegionType> regionTag;

    @Builder
    public SignUpRequest(String username, String email, String description, UUID deptId, GenderType gender, Integer grade, MbtiType mbti, LocalDate birthday, List<CategoryType> categoryTag, List<RegionType> regionTag) {
        this.username = username;
        this.email = email;
        this.description = description;
        this.deptId = deptId;
        this.gender = gender;
        this.grade = grade;
        this.mbti = mbti;
        this.birthday = birthday;
        this.categoryTag = categoryTag;
        this.regionTag = regionTag;
    }
}
