package com.haenggu.domain.entity;

import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID event_id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "started_dt")
    private LocalDateTime startedDate;

    @Column(name = "ended_dt")
    private LocalDateTime endedDate;

    @Column(name = "reservation_deadline_dt")
    private LocalDateTime reservationEndedDate;

    @Column(length = 50, name = "event_location")
    private String eventLocation;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column(length = 10)
    private RegionType region;

    @ElementCollection
    @Column(length = 50)
    private List<String> tag;
}
