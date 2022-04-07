package com.haenggu.controller;

import com.haenggu.controller.examples.BoardControllerExample;
import com.haenggu.http.request.BoardRequest;
import com.haenggu.http.response.BoardDetailResponse;
import com.haenggu.http.response.BoardListResponse;
import com.haenggu.http.response.GeneralResponse;
import com.haenggu.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "board", description = "동행글 정보 관련 API")
@Tag(name = "board like", description = "동행글 좋아요 관련 API")
@RestController
@RequestMapping("/api/boards")
public class BoardController extends BoardControllerExample {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) { this.boardService = boardService;}

    @Operation(summary = "전체 동행글 목록 조회", description = "전체 동행글 목록을 조회합니다.", tags = "board",
            responses = {
                    @ApiResponse(responseCode = "200", description = "전체 행사 목록 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardListResponse.class))),
            })
    @GetMapping
    public ResponseEntity<BoardListResponse> getBoards(
            @PageableDefault @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new BoardListResponse(pageable, boardService.findBoards(pageable)));
    }

    @Operation(summary = "동행글 등록", description = "새로운 동행글을 등록합니다.", tags = "board",
            responses = {
                    @ApiResponse(responseCode = "201", description = "동행글 정보 등록 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponse.class),
                            examples = @ExampleObject(value = BOARD_POST_SUCCESS)))
            })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<GeneralResponse> postBoard(@RequestBody BoardRequest request) {
        boardService.saveBoard(request);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.CREATED, "동행글 등록에 성공하였습니다."), HttpStatus.CREATED);
    }

    @Operation(summary = "동행글 상세 조회", description = "동행글의 ID 값을 이용하여 동행글의 상세 내용을 조회합니다.", tags = "board",
            responses = {
                    @ApiResponse(responseCode = "200", description = "동행글 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardDetailResponse.class))),
            })
    @GetMapping("/{idx}")
    public ResponseEntity<BoardDetailResponse> getBoardDetails(
            @Parameter(name = "idx", in = ParameterIn.PATH, description = "조회할 동행글의 아이디") @PathVariable("idx") UUID idx
    ) {
        return ResponseEntity.ok(boardService.findBoard(idx));
    }

    @Operation(summary = "동행글 수정", description = "동행글을 수정합니다. 동행글을 수정할 때 행사 아이디 값이 필수로 입력되어야 합니다.", tags = "board",
            responses = {
                    @ApiResponse(responseCode = "200", description = "동행글 정보 수정 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = BOARD_PUT_SUCCESS)))
            })
    @PutMapping("/{idx}")
    public ResponseEntity<GeneralResponse> patchBoard(
            @Parameter(name = "idx", in = ParameterIn.PATH, description = "조회할 동행글의 아이디") @PathVariable("idx") UUID idx,
            @RequestBody BoardRequest request
    ) {
        boardService.updateBoard(idx, request);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "동행글 수정에 성공하였습니다."), HttpStatus.OK);
    }

    @Operation(summary = "동행글 좋아요 기록 조회", description = "", tags = "board like",
            responses = {
                    @ApiResponse(responseCode = "201", description = "관심 동행글 기록을 조회합니다.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BoardListResponse.class)))
            }
    )
    @GetMapping(value = "/likes")
    public ResponseEntity<BoardListResponse> getLike(
            @PageableDefault @SortDefault(sort = "created_dt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(new BoardListResponse(pageable, boardService.findBoardLike(pageable)));
    }

    @Operation(summary = "좋아요 추가", description = "동행글에 좋아요를 추가합니다.", tags = "board like",
            responses = {
                    @ApiResponse(responseCode = "201", description = "동행글 좋아요 추가 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GeneralResponse.class)))
            }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/{idx}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> postLike(
            @Parameter(name = "idx", in = ParameterIn.PATH, description = "좋아요를 추가할 동행글의 아이디") @PathVariable("idx") UUID id
    ){
        try {
            boardService.saveBoardLike(id);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.CREATED, "좋아요 추가에 성공하였습니다."), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST, "이미 추가된 좋아요 데이터입니다."), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "좋아요 추가", description = "동행글에 좋아요를 삭제합니다.", tags = "board like",
            responses = {
                    @ApiResponse(responseCode = "204", description = "동행글 좋아요 삭제 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GeneralResponse.class)))
            }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idx}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> deleteLike(
            @Parameter(name = "idx", in = ParameterIn.PATH, description = "좋아요를 삭제할 이벤트의 아이디") @PathVariable("idx") UUID id
    ) {
        boardService.deleteBoardLike(id);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NO_CONTENT, "좋아요 삭제에 성공하였습니다."), HttpStatus.NO_CONTENT);
    }
}
