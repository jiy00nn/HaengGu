package com.haenggu.http.response.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.http.response.BoardSimpleResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDetailResponse extends EventResponse {
    @Schema(description = "행사 예약 시작 날짜", pattern = "yyyy-MM-dd'T'HH:mm:ss", example = "2021-12-18T00:00:00")
    private final LocalDateTime reservationStartedDate;
    @Schema(description = "행사 예약 종료 날짜", pattern = "yyyy-MM-dd'T'HH:mm:ss", example = "2022-04-17T00:00:00")
    private final LocalDateTime reservationEndedDate;
    @Schema(description = "행사 관람시간", example = "화~일, 10:00 ~ 19:00")
    private final String time;
    @Schema(description = "행사 장소", example = "서울 강남구 선릉로 807 K현대미술관")
    private final String eventLocation;
    @ArraySchema(schema = @Schema(implementation = String.class, description = "행사 이미지 Url", example = "https://bit.ly/3EGKodL"))
    private final List<String> imageUrl;
    @Schema(description = "회원이 행사를 즐겨찾기 선택한 여부", example = "true")
    private Boolean isFavorite;
    @ArraySchema(schema = @Schema(implementation = BoardSimpleResponse.class, description = "행사 관련 동행글"))
    private final List<BoardSimpleResponse> boards;

    public List<String> getImageUri(List<EventImage> eventImages) {
        List<String> imageUrl = new ArrayList<>();

        for (EventImage image : eventImages) {
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/events/"+ image.getEventId() + "/image/")
                    .path(image.getImageId().toString())
                    .toUriString();
            imageUrl.add(uri);
        }

        return imageUrl;
    }

    @JsonIgnore @Override
    public String getEventImageUri() {
        return super.getEventImageUri();
    }

    @JsonIgnore @Override
    public Long getFavorite() {
        return super.getFavorite();
    }

    public EventDetailResponse(Event event) {
        super(event);
        this.reservationStartedDate = event.getReservationStartedDate();
        this.reservationEndedDate = event.getReservationEndedDate();
        this.time = event.getTime() + "분";
        this.eventLocation = event.getEventLocation();
        this.imageUrl = getImageUri(event.getImage());
        this.boards = event.getBoards().stream().map(BoardSimpleResponse::new).collect(Collectors.toList());
        this.isFavorite = false;
    }
}
