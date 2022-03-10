package com.haenggu.service;

import com.haenggu.domain.entity.Board;
import com.haenggu.http.response.BoardResponse;
import com.haenggu.http.response.UserSimpleResponse;
import com.haenggu.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/users/profile/")
                                .path(board.getUser().getImage().getImageId().toString())
                                .toUriString();
        return BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .schedule(board.getSchedule().toLocalDate())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .user(UserSimpleResponse.builder()
                        .username(board.getUser().getUsername())
                        .profileImage(fileDownloadUri).build())
                .build();
    }
}
