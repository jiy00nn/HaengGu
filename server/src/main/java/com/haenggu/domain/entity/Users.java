package com.haenggu.domain.entity;

import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.RoleType;
import com.haenggu.domain.enums.SocialType;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(length = 10, columnDefinition = "char")
    private String username;

    @Column(length = 50)
    private String email;

    // 소셜 로그인의 식별값
    @Column
    private String principal;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(length = 5)
    private String gender;

    @Column
    private LocalDateTime birthday;

    @Column
    private UUID dept_id;

    @Column(length = 2)
    private Integer grade;

    @ElementCollection
    @Column(name = "event_tag", length = 50)
    private List<String> eventTag;

    @ElementCollection
    @Column(name = "region_tag", length = 50)
    private List<String> regionTag;

    @Builder
    public Users(String principal, SocialType socialType, RoleType roleType) {
        this.principal = principal;
        this.socialType = socialType;
        this.roleType = roleType;
    }
}
