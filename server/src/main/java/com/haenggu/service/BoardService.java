package com.haenggu.service;

import com.haenggu.domain.entity.Board;
import com.haenggu.http.response.BoardResponse;
import com.haenggu.http.response.UserSimpleResponse;
import com.haenggu.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private BoardResponse makeBoardResponse(Board board) {
        return BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .schedule(board.getSchedule().toLocalDate())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .user(UserSimpleResponse.builder()
                        .username(board.getUser().getUsername())
                        .profileImage("https://i.pinimg.com/564x/74/a6/45/74a6450314023cf8da27275241ee8ad8.jpg").build())
                .build();
    }
}
