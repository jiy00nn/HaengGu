package com.haenggu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private UUID imageId;

    @Column(name = "event_id")
    private UUID eventId;

    private String mimetype;

    @Column(name = "original_name")
    private String originalName;

    private Long size;

    @JsonIgnore
    private byte[] data;

    @Builder
    public EventImage(UUID eventId, String mimetype, String originalName, Long size, byte[] data) {
        this.eventId = eventId;
        this.mimetype = mimetype;
        this.originalName = originalName;
        this.size = size;
        this.data = data;
    }
}
