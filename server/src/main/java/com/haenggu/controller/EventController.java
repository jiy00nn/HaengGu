package com.haenggu.controller;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.payload.UploadFileResponse;
import com.haenggu.service.EventService;
import com.haenggu.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final FileStorageService fileStorageService;

    @Autowired
    public EventController(FileStorageService fileStorageService, EventService eventService) {
        this.fileStorageService = fileStorageService;
        this.eventService = eventService;
    }

//    @Operation(summary = "전체 행사 목록 조회", description = "전체 행사 목록을 조회합니다.")
//    @ApiResponses ({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
//    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<Event>> getEvents(@PageableDefault Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), events.getNumber(), events.getTotalElements());
        PagedModel<Event> resources = PagedModel.of(events.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(EventController.class).getEvents(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/{idx}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEvent(@PathVariable("idx") UUID idx) {
        Event event = eventService.findById(idx);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<?> postEvent(@RequestBody Event event) {
        Event result = eventService.save(event);
        System.out.println(result.getStartedDate());
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
                .path("/events/"+ idx + "/downloadFile/")
                .path(result.getImageId().toString())
                .toUriString();

        UploadFileResponse uploadFileResponse = UploadFileResponse.builder()
                .fileName(result.getOriginalName()).fileDownloadUri(fileDownloadUri)
                .fileType(result.getMimetype()).size(result.getSize())
                .build();

        return uploadFileResponse;
    }

//    @PostMapping("/{idx}/uploadMultipleFiles")
//    public @ResponseBody List<UploadFileResponse> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

//    @GetMapping(value = "/downloadFile/{fileName:.+}")
//    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            System.out.println("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        System.out.println(resource.getFilename());
//
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(new InputStreamResource(resource.getInputStream()));
//    }
}
