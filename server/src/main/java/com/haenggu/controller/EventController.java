package com.haenggu.controller;

import com.haenggu.payload.UploadFileResponse;
import com.haenggu.repository.EventRepository;
import com.haenggu.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
public class EventController {
    private final EventRepository eventRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public EventController(EventRepository eventRepository, FileStorageService fileStorageService) {
        this.eventRepository = eventRepository;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/event/upload-file")
    public @ResponseBody UploadFileResponse uploadFile(@RequestParam("file")MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        UploadFileResponse uploadFileResponse = UploadFileResponse.builder()
                .fileName(fileName).fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType()).size(file.getSize())
                .build();

        return uploadFileResponse;
    }

    @PostMapping("/event/uploadMultipleFiles")
    public @ResponseBody List<UploadFileResponse> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/event/downloadFile/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        System.out.println(resource.getFilename());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(new InputStreamResource(resource.getInputStream()));
    }

}
