package com.haenggu.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardDetailResponse {
    @Schema(description = "동행글 ID", example = "75a3f6ae-01d3-4b1b-a898-66423ade54c7")
    private String id;
    @Schema(description = "동행글 제목", example = "체리필터 단콘 티켓 구한 사람 계신가요?")
    private String title;
    @Schema(description = "동행글 내용", example = "이번에 티켓팅 성공했는데 공연 시작 전에 같이 홍대에서 저녁 먹으실 분 구해요! 6시쯤으로 생각하고 있어요! 시간 괜찮으신 분 연락 주세요 :)")
    private String content;
    @Schema(description = "동행글 일정", example = "2021.12.23")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate schedule;
    @Schema(description = "동행글 관련 이벤트 정보")
    private BoardEventSimpleResponse event;
    @Schema(description = "동행글 작성자 정보")
    private UserTagsResponse user;
    @Schema(description = "동행글 즐겨찾기 여부", example = "false")
    private Boolean isFavorite;

    public void setConcert(String id, String title) {
        this.event = new BoardEventSimpleResponse(id, title);
    }

    public void setUser(String id, String username, String profileImage, List<String> tags) {
        this.user = new UserTagsResponse(id, username, profileImage, tags);
    }

    @Getter
    @AllArgsConstructor
    private static class BoardEventSimpleResponse {
        @Schema(description = "이벤트 ID", example = "ebafbf80-bd1c-4b82-a488-0394161b0cc4")
        private String id;
        @Schema(description = "이벤트 제목", example = "체리필터 연말 단독 콘서트 : Cherry Christmas")
        private String title;
    }

    @Getter
    @AllArgsConstructor
    private static class UserTagsResponse {
        @Schema(description = "동행글 작성자 ID", example = "6fcd1647-d640-488c-8497-7dbff946f2be")
        private String id;
        @Schema(description = "동행글 작성자 이름", example = "낭만고양이")
        private String username;
        @Schema(description = "동행글 작성자 프로필 이미지 uri", example = "http://34.64.215.171:8080/api/users/profile/90d2235f-ecd3-43ca-8756-fc5d42434682")
        private String profileImage;
        @Schema(description = "동행글 작성자 태그")
        private List<String> tags;
    }
}
