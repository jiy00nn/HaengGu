package com.haenggu.controller;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import com.haenggu.http.response.EventListResponse;
import com.haenggu.http.response.EventResponse;
import com.haenggu.http.response.GeneralResponse;
import com.haenggu.http.response.UploadFileResponse;
import com.haenggu.service.EventService;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.util.Arrays;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "event", description = "행사 정보 관련 API")
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "전체 행사 목록 조회", description = "전체 행사 목록을 조회합니다.", tags = "event",
               responses = {
                   @ApiResponse(responseCode = "200", description = "전체 행사 목록 조회 성공",
                                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventListResponse.class))),
               })
    @GetMapping
    public ResponseEntity<EventListResponse> getEvents(@PageableDefault Pageable pageable,
                                                       @Parameter(name = "category", in = ParameterIn.QUERY, description = "행사 카테고리") @RequestParam(value="category", required = false) CategoryType categoryType,
                                                       @Parameter(name = "region", in = ParameterIn.QUERY, description = "지역 카테고리") @RequestParam(value="region", required = false) RegionType regionType) {
        Page<EventResponse> events;
        if (categoryType != null && regionType != null) {
            events = eventService.findAll(pageable);
        } else if (categoryType != null) {
            events = eventService.findByCategory(categoryType, pageable);
        } else if (regionType != null) {
            events = eventService.findAll(pageable);
        } else {
            events = eventService.findAll(pageable);
        }

        EventListResponse eventListResponse = new EventListResponse(pageable, events);
        return ResponseEntity.ok(eventListResponse);
    }

    @Operation(summary = "행사 정보 상세 조회", description = "아이디를 통해 행사의 상세 정보를 조회합니다.", tags = "event",
               responses = {
                    @ApiResponse(responseCode = "200", description = "행사 상세 정보 조회 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class))),
               })
    @GetMapping(value = "/{idx}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> getEvent(@Parameter(name = "idx", in = ParameterIn.PATH, description = "행사 아이디") @PathVariable("idx") UUID idx) {
        EventResponse response = eventService.findById(idx);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "행사 정보 등록", description = "새로운 행사 정보를 등록합니다.", tags = "event",
               responses = {
                    @ApiResponse(responseCode = "201", description = "행사 정보 등록 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
               })
    @PostMapping
    public ResponseEntity<Event> postEvent(@RequestBody Event event) {
        Event result = eventService.save(event);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "행사 정보 수정", description = "행사의 전체 정보를 수정합니다.", tags = "event",
            responses = {
                    @ApiResponse(responseCode = "200", description = "행사 정보 수정 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
                    @ApiResponse(responseCode = "404", description = "잘못된 아이디 값으로 접근",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponse.class),
                                                    examples = @ExampleObject(value = EVENT_NOT_FOUND)))
            })
    @PutMapping(value = "/{idx}")
    public ResponseEntity<?> putEvent(@Parameter(name = "idx", in = ParameterIn.PATH, description = "수정할 행사의 아이디") @PathVariable("idx")UUID idx, @RequestBody Event event) {
        if (eventService.update(idx, event))
            return new ResponseEntity<>(event, HttpStatus.OK);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND, "접근할 수 없는 아이디값입니다."), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "행사 정보 삭제", description = "행사 정보를 삭제합니다.", tags = "event",
               responses = {
                    @ApiResponse(responseCode = "200", description = "행사 정보 삭제 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponse.class),
                                 examples = @ExampleObject(value = EVENT_DELETE_SUCCESS)))
               })
    @DeleteMapping(value = "/{idx}")
    public  ResponseEntity<GeneralResponse> deleteBoard(@Parameter(name = "idx", in = ParameterIn.PATH, description = "삭제할 행사의 아이디") @PathVariable("idx")UUID idx) {
        eventService.deleteById(idx);
        return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "행사 정보 삭제 성공"), HttpStatus.OK);
    }

    @Operation(summary = "행사 이미지 등록", description = "행사의 이미지를 등록합니다.", tags = "event",
               responses = {
                    @ApiResponse(responseCode = "200", description = "행사 이미지 등록 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadFileResponse.class)))
               })
    @PostMapping("/{idx}/upload-file")
    public @ResponseBody UploadFileResponse uploadFile(@Parameter(name = "idx", in = ParameterIn.PATH, description = "이미지를 추가할 행사의 아이디") @PathVariable("idx") UUID idx,
                                                       @RequestBody MultipartFile file) {
        EventImage result = eventService.storeFile(idx, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/events/"+ idx + "/image/")
                .path(result.getImageId().toString())
                .toUriString();

        UploadFileResponse uploadFileResponse = UploadFileResponse.builder()
                .fileName(result.getOriginalName()).fileDownloadUri(fileDownloadUri)
                .fileType(result.getMimetype()).size(result.getSize())
                .build();

        return uploadFileResponse;
    }

    @Operation(summary = "다중 행사 이미지 등록", description = "여러개의 행사의 이미지를 등록합니다.", tags = "event",
            responses = {
                    @ApiResponse(responseCode = "200", description = "행사 이미지 등록 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadFileResponse.class)))
            })
    @PostMapping("/{idx}/uploadMultipleFiles")
    public @ResponseBody List<UploadFileResponse> uploadMultipleFile(@PathVariable("idx") UUID idx, @RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(idx, file))
                .collect(Collectors.toList());
    }

    @Operation(summary = "행사 이미지 조회", description = "행사 아이디와 이미지 아이디를 통해 이미지를 얻습니다.", tags = "event",
            responses = {
                    @ApiResponse(responseCode = "200", description = "행사 이미지 조회 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = byte.class),
                                 examples = @ExampleObject(value = "byte value")))
            })
    @GetMapping("/{idx}/image/{imageId}")
    public @ResponseBody ResponseEntity<?> downloadFile(@Parameter(name = "idx", in = ParameterIn.PATH, description = "행사 아이디") @PathVariable("idx") UUID eventIdx,
                                                        @Parameter(name = "imageId", in = ParameterIn.PATH, description = "이미지 아이디") @PathVariable("imageId")UUID idx){
        EventImage image = eventService.loadFileAsByte(idx);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }

    private static final String EVENT_DELETE_SUCCESS    = "{\n" +
                                                          "    \"code\" : 204\n" +
                                                          "    \"message\" : \"행사 정보 삭제에 성공하였습니다.\"\n" +
                                                          "}";
    private static final String EVENT_NOT_FOUND         = "{\n" +
                                                          "    \"code\" : 404\n" +
                                                          "    \"message\" : \"접근할 수 없는 아이디값입니다.\"\n" +
                                                          "}";
}
