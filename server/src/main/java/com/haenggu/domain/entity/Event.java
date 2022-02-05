package com.haenggu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.haenggu.domain.BaseTimeEntity;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table
@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    @JsonProperty(value = "event_id")
    private UUID eventId;

    @Column(length = 50, nullable = false)
    @JsonProperty(value = "title")
    private String title;

    @Column(length = 2000)
    @JsonProperty(value = "description")
    private String description;

    @Column(name = "started_dt")
    @JsonProperty(value = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "ended_dt")
    @JsonProperty(value = "ended_date")
    private LocalDateTime endedDate;

    @Column(name = "reservation_deadline_dt")
    @JsonProperty(value = "reservation_deadline_date")
    private LocalDateTime reservationEndedDate;

    @Column(length = 50, name = "event_location")
    @JsonProperty(value = "event_location")
    private String eventLocation;

    @Column(length = 20, nullable = false)
    @JsonProperty(value = "category")
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column(length = 10)
    @JsonProperty(value = "region")
    private RegionType region;

    @ElementCollection
    @Column(length = 50)
    @JsonProperty(value = "tag")
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
