package com.haenggu.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponse {
    private String id;
    private String title;
    private String content;
    private EventTitleAndImageResponse event;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate schedule;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private UserSimpleResponse user;

    public void setEvent(Event event) {
        String eventImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/events/"+ event.getEventId() + "/image/")
                .path(event.getImage().get(0).getImageId().toString())
                .toUriString();
        this.event = new EventTitleAndImageResponse(event.getTitle(), eventImageUrl);
    }

    @Getter
    @AllArgsConstructor
    private static class EventTitleAndImageResponse {
        @Schema(description = "이벤트 제목", example = "체리필터 연말 단독 콘서트 : Cherry Christmas")
        private String title;
        @Schema(description = "이벤트 이미지", example = "http://34.64.215.171:8080/api/events/ebafbf80-bd1c-4b82-a488-0394161b0cc4/image/433e6003-de01-459b-b148-9df73020c746")
        private String event_image;
    }
}
