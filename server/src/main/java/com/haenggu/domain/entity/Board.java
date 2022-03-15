package com.haenggu.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Board extends BaseTimeEntity implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID boardId;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "schedule")
    private LocalDate schedule;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public Board(String title, String content, LocalDate schedule, Event event, Users user) {
        this.title = title;
        this.content = content;
        this.schedule = schedule;
        this.event = event;
        this.user = user;
    }
}
