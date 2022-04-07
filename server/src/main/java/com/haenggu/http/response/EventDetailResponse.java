package com.haenggu.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDetailResponse {
    private UUID eventId;
    private String title;
    private String description;
    private Long favorite;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime startedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime endedDate;
    private LocalDateTime reservationStartedDate;
    private LocalDateTime reservationEndedDate;
    private Integer time;
    private String eventLocation;
    private CategoryType category;
    private RegionType region;
    private List<String> tag;
    private List<String> imageUrl;
    private List<BoardSimpleResponse> boards;

    public void addTag(String tag) {
        this.tag.add(tag);
    }

    public void setBoards(List<BoardSimpleResponse> boards) {
        this.boards = boards;
    }

    @Builder
    public EventDetailResponse(UUID eventId, String title, String description, Long favorite, LocalDateTime startedDate, LocalDateTime endedDate,
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
