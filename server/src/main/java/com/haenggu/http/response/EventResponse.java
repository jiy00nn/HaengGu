package com.haenggu.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventResponse {
    private final UUID eventId;
    private final String title;
    private final String description;
    private final Integer favorite;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDateTime startedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDateTime endedDate;
    private final LocalDateTime reservationStartedDate;
    private final LocalDateTime reservationEndedDate;
    private final Integer time;
    private final String eventLocation;
    private final CategoryType category;
    private final RegionType region;
    private final List<String> tag;
    private final List<String> imageUrl;

    public void addTag(String tag) {
        this.tag.add(tag);
    }

    @Builder
    public EventResponse(UUID eventId, String title, String description, Integer favorite, LocalDateTime startedDate, LocalDateTime endedDate,
                         LocalDateTime reservationStartedDate, LocalDateTime reservationEndedDate, Integer time, String eventLocation,
                         CategoryType category, RegionType region, List<String> tag, List<String> imageUrl) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.favorite = favorite;
        this.startedDate = startedDate;
        this.endedDate = endedDate;
        this.reservationStartedDate = reservationStartedDate;
        this.reservationEndedDate = reservationEndedDate;
        this.time = time;
        this.eventLocation = eventLocation;
        this.category = category;
        this.region = region;
        this.tag = tag;
        this.imageUrl = imageUrl;
    }
}
