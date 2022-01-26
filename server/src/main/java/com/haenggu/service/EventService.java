package com.haenggu.service;

import com.haenggu.configuration.FileStorageProperties;
import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.exception.FileStorageException;
import com.haenggu.repository.EventImageRepository;
import com.haenggu.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    //    private final Path fileStorageLocation;
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    @Autowired
    public EventService(FileStorageProperties fileStorageProperties, EventRepository eventRepository, EventImageRepository eventImageRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
//
//        try{
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception e) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
//        }
    }

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event findById(UUID idx) {
        return eventRepository.getEventByEventId(idx);
    }

    public Event save(Event event) {
        Event result = eventRepository.save(event);
        return result;
    }

    @Transactional
    public Boolean update(UUID idx, Event event) {
        Optional<Event> optionalEvent = eventRepository.findEventByEventId(idx);
        if(optionalEvent.isEmpty())    return Boolean.FALSE;

        Event persistEvent = optionalEvent.get();

        if(!Objects.isNull(event.getTitle())) {
            persistEvent.setTitle(event.getTitle());
        }
        if(!Objects.isNull(event.getDescription())) {
            persistEvent.setDescription(event.getDescription());
        }
        if(!Objects.isNull(event.getStartedDate())) {
            persistEvent.setStartedDate(event.getStartedDate());
        }
        if(!Objects.isNull(event.getEndedDate())) {
            persistEvent.setEndedDate(event.getEndedDate());
        }
        if(!Objects.isNull(event.getReservationEndedDate())) {
            persistEvent.setReservationEndedDate(event.getReservationEndedDate());
        }
        if(!Objects.isNull(event.getEventLocation())) {
            persistEvent.setEventLocation(event.getEventLocation());
        }
        if(!Objects.isNull(event.getCategory())) {
            persistEvent.setCategory(event.getCategory());
        }
        if(!Objects.isNull(event.getRegion())) {
            persistEvent.setRegion(event.getRegion());
        }
        if(!Objects.isNull(event.getTag())) {
            persistEvent.setTag(event.getTag());
        }
        eventRepository.save(persistEvent);

        return Boolean.TRUE;
    }

    public void deleteById(UUID idx) {
        eventRepository.deleteEventByEventId(idx);
    }

    public EventImage storeFile(UUID eventId, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            // Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            EventImage eventImage =
                    EventImage.builder()
                            .eventId(eventId)
                            .mimetype(file.getContentType())
                            .originalName(file.getOriginalFilename())
                            .size(file.getSize())
                            .data(file.getBytes())
                            .build();
            EventImage result = eventImageRepository.save(eventImage);

            return result;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

//    public Resource loadFileAsResource(String fileName) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if(resource.exists()) {
//                return resource;
//            } else {
//                throw new MyFileNotFoundException("File not found " + fileName);
//            }
//
//        } catch (MalformedURLException e) {
//            throw new MyFileNotFoundException("File not found " + fileName, e);
//        }
//    }
}
