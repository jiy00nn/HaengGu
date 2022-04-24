package com.haenggu.http.response.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "행사 기본 정보 응답 DTO")
@Getter @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventBasicResponse extends BasicTimeResponse {
    private static final String EVENT_ID = "c4b5ba13-a754-41e8-8a5f-6021cb4bbbc1";
    private static final String EVENT_TITLE = "데이비드 슈리글리 개인전";
    private static final String EVENT_IMAGE_URI = "https://bit.ly/3EGKodL";

    @Schema(description = "행사 아이디", example = EVENT_ID)
    private final UUID eventId;
    @Schema(description = "행사 제목", example = EVENT_TITLE)
    private final String title;
    @Schema(description = "행사 대표 이미지 URI", example = EVENT_IMAGE_URI)
    private final String eventImageUri;

    public EventBasicResponse(Event event) {
        super(event.getStartedDate(), event.getEndedDate());
        this.eventId = event.getEventId();
        this.title = event.getTitle();
        this.eventImageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/events/"+eventId.toString()+"/image")
                .path(event.getImage().get(0).getImageId().toString())
                .toUriString();
    }

    public EventBasicResponse(UUID eventId, String title, LocalDateTime startedDate, LocalDateTime endedDate) {
        super(startedDate, endedDate);
        this.eventId = eventId;
        this.title = title;
        this.eventImageUri = "";
    }
}
