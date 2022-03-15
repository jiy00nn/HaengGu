package com.haenggu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "dept_id")
    private School school;

    @Column(length = 2)
    private Integer grade;

    @Column(length = 4)
    @Enumerated(EnumType.STRING)
    private MbtiType mbti;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "event_tag", length = 50)
    private List<CategoryType> eventTag;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "region_tag", length = 50)
    private List<RegionType> regionTag;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private UserImage image;

    public List<String> getUserTags() {
        LocalDate now = LocalDate.now();
        List<String> tags = new ArrayList<String>();
        tags.add(this.gender.getValue());
        tags.add(String.valueOf(now.getYear() - birthday.getYear() + 1) + "살");
        tags.add(this.getSchool().getSchoolName());
        tags.add(this.getSchool().getDeptName());
        tags.add(this.grade.toString() + "학년");
        tags.add(this.mbti.getValue());
        tags.addAll(this.eventTag.stream().map(CategoryType::getValue).collect(Collectors.toList()));
        tags.addAll(this.regionTag.stream().map(RegionType::getValue).collect(Collectors.toList()));
        return tags;
    }

    @Builder
    public Users(String username, String email, String principal, SocialType socialType, RoleType roleType, GenderType gender, LocalDate birthday, School school, Integer grade, MbtiType mbti, List<CategoryType> eventTag, List<RegionType> regionTag) {
        this.username = username;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.roleType = roleType;
        this.gender = gender;
        this.birthday = birthday;
        this.school = school;
        this.grade = grade;
        this.mbti = mbti;
        this.eventTag = eventTag;
        this.regionTag = regionTag;
    }
}
