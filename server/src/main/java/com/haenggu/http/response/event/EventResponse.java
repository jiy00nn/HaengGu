package com.haenggu.http.response.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventResponse extends EventBasicResponse {
    private final String description;
    private final Long favorite;
    private final List<String> tag;

    public EventResponse(Event event) {
        this(event, Long.getLong(String.valueOf(0)));
    }

    @Builder
    public EventResponse(Event event, Long favorite) {
        super(event.getEventId(), event.getTitle(), event.getStartedDate(), event.getEndedDate());
        this.description = event.getDescription();
        this.favorite = favorite;
        this.tag = event.getTag();
        this.tag.add(event.getRegion().getValue());
    }
}
