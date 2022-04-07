package com.haenggu.service;

import com.haenggu.domain.entity.Board;
import com.haenggu.domain.entity.Users;
import com.haenggu.http.request.BoardRequest;
import com.haenggu.http.response.BoardDetailResponse;
import com.haenggu.http.response.BoardResponse;
import com.haenggu.http.response.UserSimpleResponse;
import com.haenggu.repository.BoardRepository;
import com.haenggu.repository.EventRepository;
import com.haenggu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(UserRepository userRepository, EventRepository eventRepository, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.boardRepository = boardRepository;
    }

    public Page<BoardResponse> findBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(this::makeBoardResponse);
    }

    public BoardDetailResponse findBoard(UUID id) {
        return makeBoardDetailsResponse(boardRepository.getById(id));
    }

    public void saveBoard(BoardRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .schedule(request.getSchedule())
                .event(eventRepository.getEventByEventId(request.getEventId()))
                .user(userRepository.getById(UUID.fromString(authentication.getPrincipal().toString())))
                .build();
        boardRepository.save(board);
    }

    public void updateBoard(UUID id, BoardRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.getById(UUID.fromString(authentication.getPrincipal().toString()));
        Optional<Board> board = boardRepository.findBoardByBoardIdAndUser(id, user);
        board.ifPresent(data -> {
            if(request.getEventId() == null) {
                throw new RuntimeException();
            }
            data.setEvent(eventRepository.getById(request.getEventId()));
            data.setTitle(request.getTitle());
            data.setContent(request.getContent());
            data.setSchedule(request.getSchedule());
            boardRepository.save(data);
        });
    }

    private BoardResponse makeBoardResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getBoardId().toString())
                .title(board.getTitle())
                .content(board.getContent())
                .eventTitle(board.getEvent().getTitle())
                .schedule(board.getSchedule())
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
                .schedule(board.getSchedule())
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
