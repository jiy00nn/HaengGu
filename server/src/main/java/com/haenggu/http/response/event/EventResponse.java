package com.haenggu.http.response.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventResponse extends EventBasicResponse {
    private static final String EVENT_DESCRIPTION = "형식에 얽매이지 않고 다양한 예술적인 실험을 하는 영국의 일러스트 아티스트 " +
            "데이비드 슈리글리의 드로잉, 조각, 애니메이션 등이 선보여지는 전시입니다.";
    private static final String EVENT_FAVORITE = "10";
    
    @Schema(description = "행사 상세 설명", example = EVENT_DESCRIPTION)
    private final String description;
    @Schema(description = "행사 좋아요 갯수", example = EVENT_FAVORITE)
    private final Long favorite;
    @Schema(description = "행사 태그 정보", allOf = String.class)
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
