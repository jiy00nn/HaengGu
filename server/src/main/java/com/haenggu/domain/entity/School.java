package com.haenggu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dept_id")
    private UUID deptId;

    @JsonIgnore
    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "dept_name")
    @Schema(example = "컴퓨터공학과")
    private String deptName;
}
