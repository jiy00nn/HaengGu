package com.haenggu.http.response;

import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class EventResponse {
    private UUID eventId;
    private String title;
    private String description;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private LocalDateTime reservationEndedDate;
    private String eventLocation;
    private CategoryType category;
    private RegionType region;
    private List<String> tag;
    private List<String> imageUrl;

    @Builder
    public EventResponse(UUID eventId, String title, String description, LocalDateTime startedDate, LocalDateTime endedDate, LocalDateTime reservationEndedDate, String eventLocation, CategoryType category, RegionType region, List<String> tag, List<String> imageUrl) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startedDate = startedDate;
        this.endedDate = endedDate;
        this.reservationEndedDate = reservationEndedDate;
        this.eventLocation = eventLocation;
        this.category = category;
        this.region = region;
        this.tag = tag;
        this.imageUrl = imageUrl;
    }
}
