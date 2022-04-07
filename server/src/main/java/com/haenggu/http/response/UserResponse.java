package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private final String profileUri;
    private final String username;
    private final String description;
    private final List<String> tags;
    private final List<BoardResponse> boards;
    private final List<EventSimpleResponse> events;

    @Builder
    public UserResponse(UUID imageId, String username, String description, List<String> tags, List<Board> boards, List<EventSimpleResponse> events) {
        this.profileUri = makeProfileUri(imageId);
        this.username = username;
        this.description = description;
        this.tags = tags;
        this.boards = boards.stream().map(this::makeBoardResponse).collect(Collectors.toList());
        this.events = events;
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
