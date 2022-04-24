package com.haenggu.http.response.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "행사 시작 및 종료 일정 응답 DTO")
@Getter @RequiredArgsConstructor
public class BasicTimeResponse {
    private final static String EVENT_STARTED_DATE = "2021.12.18";
    private final static String EVENT_ENDED_DATE = "2022.04.17";

    @Schema(description = "행사 시작 날짜", pattern = "yyyy.MM.dd", example = EVENT_STARTED_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDateTime startedDate;
    @Schema(description = "행사 종료 날짜", pattern = "yyyy.MM.dd", example = EVENT_ENDED_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDateTime endedDate;
}
