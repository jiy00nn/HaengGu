package com.haenggu.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "dept_name")
    private String deptName;
}
