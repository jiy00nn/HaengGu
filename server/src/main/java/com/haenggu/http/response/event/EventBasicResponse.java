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


    private final UUID eventId;
    private final String title;
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
