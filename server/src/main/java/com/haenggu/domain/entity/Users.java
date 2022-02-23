package com.haenggu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.*;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Users extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private UUID userId;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String email;

    // 소셜 로그인의 식별값
    @Column
    @JsonIgnore
    private String principal;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(length = 6)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column
    private LocalDateTime birthday;

    @Column
    private UUID deptId;

    @Column(length = 2)
    private Integer grade;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "event_tag", length = 50)
    private List<CategoryType> eventTag;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "region_tag", length = 50)
    private List<RegionType> regionTag;

    @Builder
    public Users(String username, String email, String principal, SocialType socialType, RoleType roleType, GenderType gender, LocalDateTime birthday, UUID deptId, Integer grade, List<CategoryType> eventTag, List<RegionType> regionTag) {
        this.username = username;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.roleType = roleType;
        this.gender = gender;
        this.birthday = birthday;
        this.deptId = deptId;
        this.grade = grade;
        this.eventTag = eventTag;
        this.regionTag = regionTag;
    }
}
