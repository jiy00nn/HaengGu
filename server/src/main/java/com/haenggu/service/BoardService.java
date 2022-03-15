package com.haenggu.service;

import com.haenggu.domain.entity.Board;
import com.haenggu.domain.entity.Users;
import com.haenggu.http.response.BoardDetailResponse;
import com.haenggu.http.response.BoardResponse;
import com.haenggu.http.response.UserSimpleResponse;
import com.haenggu.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<BoardResponse> findBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(this::makeBoardResponse);
    }

    public BoardDetailResponse findBoard(UUID id) {
        return makeBoardDetailsResponse(boardRepository.getById(id));
    }

    private BoardResponse makeBoardResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getBoardId().toString())
                .title(board.getTitle())
                .content(board.getContent())
                .schedule(board.getSchedule().toLocalDate())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .user(UserSimpleResponse.builder()
                        .username(board.getUser().getUsername())
                        .profileImage(makeProfileUri(board.getUser().getImage().getImageId())).build())
                .build();
    }

    private BoardDetailResponse makeBoardDetailsResponse(Board board) {
        BoardDetailResponse response = BoardDetailResponse.builder()
                .id(board.getBoardId().toString())
                .title(board.getTitle())
                .content(board.getContent())
                .schedule(board.getSchedule().toLocalDate())
                .isFavorite(false)
                .build();
        response.setConcert(board.getEvent().getEventId().toString(), board.getEvent().getTitle());

        Users user = board.getUser();

        response.setUser(user.getUserId().toString(), user.getUsername(),
                        makeProfileUri(user.getImage().getImageId()), user.getUserTags());

        return response;
    }

    private String makeProfileUri(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/profile/")
                .path(id.toString())
                .toUriString();
    }
}
