package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private String profileUri;
    private String username;
    private String description;
    private List<String> tags;
    private List<BoardResponse> boards;
    private List<EventResponse> events;

    @Builder
    public UserResponse(UUID imageId, String username, String description, List<String> tags, List<Board> boards) {
        this.profileUri = makeProfileUri(imageId);
        this.username = username;
        this.description = description;
        this.tags = tags;;
        this.boards = boards.stream().map(this::makeBoardResponse).collect(Collectors.toList());
        List<EventResponse> event = new ArrayList<>();
        event.add(new EventResponse());
        event.add(new EventResponse());
        this.events = event;
    }

    @Getter
    private class EventResponse {
        String eventImageUri;
        String title;
        LocalDate startedDate;
        LocalDate endedDate;

        public EventResponse() {
            this.eventImageUri = "https://i.imgur.com/g9xOODy.png";
            this.title = "옥상달빛 연말 단독 공연［수고했어，올해도 2021］";
            this.startedDate = LocalDate.of(2021,12,10);
            this.endedDate = LocalDate.of(2021,12,12);
        }
    }

    private BoardResponse makeBoardResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getBoardId().toString())
                .title(board.getTitle())
                .content(board.getContent())
                .schedule(board.getSchedule())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .user(UserSimpleResponse.builder()
                        .username(board.getUser().getUsername())
                        .profileImage(makeProfileUri(board.getUser().getImage().getImageId())).build())
                .build();
    }

    private String makeProfileUri(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/profile/")
                .path(id.toString())
                .toUriString();
    }
}
