package com.haenggu.controller;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.payload.EventResponse;
import com.haenggu.payload.UploadFileResponse;
import com.haenggu.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

//    @Operation(summary = "전체 행사 목록 조회", description = "전체 행사 목록을 조회합니다.")
//    @ApiResponses ({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
//    })
    @GetMapping
    public ResponseEntity<PagedModel> getEvents(@PageableDefault Pageable pageable) {
        Page<EventResponse> events = eventService.findAll(pageable);
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), events.getNumber(), events.getTotalElements());
        PagedModel<EventResponse> resources = PagedModel.of(events.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(EventController.class).getEvents(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/{idx}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEvent(@PathVariable("idx") UUID idx) {
        EventResponse response = eventService.findById(idx);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> postEvent(@RequestBody Event event) {
        Event result = eventService.save(event);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{idx}")
    public ResponseEntity<?> putEvent(@PathVariable("idx")UUID idx, @RequestBody Event event) {
        if (eventService.update(idx, event))
            return new ResponseEntity<>(event, HttpStatus.OK);
        return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{idx}")
    public  ResponseEntity<?> deleteBoard(@PathVariable("idx")UUID idx) {
        eventService.deleteById(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PostMapping("/{idx}/upload-file")
    public @ResponseBody UploadFileResponse uploadFile(@PathVariable("idx") UUID idx, @RequestParam("file")MultipartFile file) {
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

    @PostMapping("/{idx}/uploadMultipleFiles")
    public @ResponseBody List<UploadFileResponse> uploadMultipleFile(@PathVariable("idx") UUID idx, @RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(idx, file))
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}/image/{imageId}")
    public @ResponseBody ResponseEntity<?> downloadFile(@PathVariable("eventId")UUID eventIdx, @PathVariable("imageId")UUID idx){
        EventImage image = eventService.loadFileAsByte(idx);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(image.getMimetype()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }
}
