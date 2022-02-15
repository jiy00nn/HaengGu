package com.haenggu.domain.entity;

import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.RoleType;
import com.haenggu.domain.enums.SocialType;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID user_id;

    @NotNull
    @Column(length = 10, columnDefinition = "char")
    private String username;

    @Column(length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

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
    public Users(String username, String email, RoleType roleType, SocialType socialType, String gender, LocalDateTime birthday, UUID dept_id, Integer grade, List<String> eventTag, List<String> regionTag) {
        this.username = username;
        this.email = email;
        this.roleType = roleType;
        this.socialType = socialType;
        this.gender = gender;
        this.birthday = birthday;
        this.dept_id = dept_id;
        this.grade = grade;
        this.eventTag = eventTag;
        this.regionTag = regionTag;
    }

    public String getRoleKey() {
        return this.roleType.getKey();
    }
}
