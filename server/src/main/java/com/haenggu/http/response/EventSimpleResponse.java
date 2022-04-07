package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventSimpleResponse {
    UUID eventId;
    String eventImageUri;
    String title;
    LocalDate startedDate;
    LocalDate endedDate;

    public EventSimpleResponse(Event event) {
        this.eventId = event.getEventId();
        this.eventImageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/events/"+event.getEventId().toString()+"/image")
                .path(event.getImage().get(0).getImageId().toString())
                .toUriString();
        this.title = event.getTitle();
        this.startedDate = event.getStartedDate().toLocalDate();
        this.endedDate = event.getEndedDate().toLocalDate();
    }
}
