package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Board;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardSimpleResponse {
    private final UUID boardId;
    private final String title;
    private final UserSimpleResponse user;

    public BoardSimpleResponse(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.user = new UserSimpleResponse(board.getUser());
    }
}
