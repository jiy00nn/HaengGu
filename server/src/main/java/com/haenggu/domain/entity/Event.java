package com.haenggu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table
@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID eventId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "started_dt")
    @Schema(name = "started_date", example = "")
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
    @Enumerated(EnumType.STRING)
    private RegionType region;

    @ElementCollection
    @Column(length = 50)
    private List<String> tag;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private List<EventImage> image;

    @Builder
    public Event(String title, String description, LocalDateTime startedDate, LocalDateTime endedDate, LocalDateTime reservationEndedDate, String eventLocation, CategoryType category, RegionType region, List<String> tag) {
        this.title = title;
        this.description = description;
        this.startedDate = startedDate;
        this.endedDate = endedDate;
        this.reservationEndedDate = reservationEndedDate;
        this.eventLocation = eventLocation;
        this.category = category;
        this.region = region;
        this.tag = tag;
    }
}
