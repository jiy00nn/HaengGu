package com.haenggu.domain;

import com.haenggu.domain.enums.CategoryType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Event implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID event_id;

    @Column(length = 50)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "started_at")
    private LocalDateTime startedDate;

    @Column(name = "ended_at")
    private LocalDateTime endedDate;

    @Column(name = "reservation_ended_at")
    private LocalDateTime reservationEndedDate;

    @Column(length = 50, name = "event_location")
    private String eventLocation;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column(length = 50)
    private String region;

    @ElementCollection
    @Column(length = 50)
    private List<String> tag;

    public Event(String title, String description, CategoryType category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    @Builder
    public Event(UUID event_id, String title, String description, LocalDateTime startedDate, LocalDateTime endedDate, LocalDateTime reservationEndedDate, String eventLocation, CategoryType category, String region, List<String> tag) {
        this.event_id = event_id;
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
