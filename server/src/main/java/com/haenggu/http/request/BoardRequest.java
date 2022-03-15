package com.haenggu.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardRequest {
    @Schema(description = "동행글 관련 행사 ID")
    UUID eventId;
    @Schema(description = "동행 일정")
    LocalDate schedule;
    @Schema(description = "동행글 제목", example = "체리필터 단콘 티켓 구한 사람 계신가요?")
    String title;
    @Schema(description = "동행글 내용", example = "이번에 티켓팅 성공했는데 공연 시작 전에 같이 홍대에서 저녁 먹으실 분 구해요! 6시쯤으로 생각하고 있어요! 시간 괜찮으신 분 연락 주세요 :)")
    String content;
}
