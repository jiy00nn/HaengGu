package com.haenggu.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.GenderType;
import com.haenggu.domain.enums.RegionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
    private final String username;
    private final String email;
    private final UUID deptId;
    private final GenderType gender;
    private final Integer grade;
    private final LocalDateTime birthday;
    private final List<CategoryType> categoryTag;
    private final List<RegionType> regionTag;

    @Builder
    public SignUpRequest(String username, String email, UUID deptId, GenderType gender, Integer grade, LocalDateTime birthday, List<CategoryType> categoryTag, List<RegionType> regionTag) {
        this.username = username;
        this.email = email;
        this.deptId = deptId;
        this.gender = gender;
        this.grade = grade;
        this.birthday = birthday;
        this.categoryTag = categoryTag;
        this.regionTag = regionTag;
    }
}
