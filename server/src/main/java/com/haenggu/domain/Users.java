package com.haenggu.domain;

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
public class Users {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID user_id;

    @NotNull
    @Column(length = 10, columnDefinition = "char")
    private String username;

    @Column(length = 30)
    private String password;

    @Column(length = 50)
    private String email;

    @Column
    private String principal;

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

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedDate;

    @Builder
    public Users(UUID user_id, String username, String password, String email, String principal, SocialType socialType, String gender, LocalDateTime birthday, UUID dept_id, Integer grade, List<String> eventTag, List<String> regionTag, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.gender = gender;
        this.birthday = birthday;
        this.dept_id = dept_id;
        this.grade = grade;
        this.eventTag = eventTag;
        this.regionTag = regionTag;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
