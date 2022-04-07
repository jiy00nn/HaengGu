package com.haenggu.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Schema(description = "행사 좋아요")
@Getter
@Entity
@Table(name = "event_like", uniqueConstraints = {@UniqueConstraint(name = "event_user_uk", columnNames = {"user_id", "event_id"})})
@NoArgsConstructor
public class EventLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_like_id")
    @Schema(description = "행사 좋아요 아이디", nullable = false)
    private UUID eventLikeId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public EventLike(Event event, Users user) {
        this.event = event;
        this.user = user;
    }
}
