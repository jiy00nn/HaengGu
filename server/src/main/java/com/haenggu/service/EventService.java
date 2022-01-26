package com.haenggu.service;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.exception.FileStorageException;
import com.haenggu.payload.EventResponse;
import com.haenggu.repository.EventImageRepository;
import com.haenggu.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    @Autowired
    public EventService(EventRepository eventRepository, EventImageRepository eventImageRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public Page<EventResponse> findAll(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        Page<EventResponse> eventResponses = events.map(event ->  makeEventResponse(event));
        return eventResponses;
    }

    public EventResponse findById(UUID idx) {
        Event event = eventRepository.getEventByEventId(idx);
        return makeEventResponse(event);
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

    public EventImage loadFileAsByte(UUID imageId) {
        return eventImageRepository.getEventImageByImageId(imageId);
    }

    public EventResponse makeEventResponse(Event event) {
        return EventResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startedDate(event.getStartedDate())
                .endedDate(event.getEndedDate())
                .reservationEndedDate(event.getReservationEndedDate())
                .eventLocation(event.getEventLocation())
                .category(event.getCategory())
                .region(event.getRegion())
                .tag(event.getTag())
                .imageUrl(getImageUri(event.getImage()))
                .build();
    }

    public List<String> getImageUri(List<EventImage> eventImages) {
        List<String> imageUrl = new ArrayList<>();

        for (EventImage image : eventImages) {
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/events/"+ image.getEventId() + "/image/")
                    .path(image.getImageId().toString())
                    .toUriString();
            imageUrl.add(uri);
        }

        return imageUrl;
    }
}
