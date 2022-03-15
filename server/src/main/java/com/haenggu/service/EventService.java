package com.haenggu.service;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import com.haenggu.exception.FileStorageException;
import com.haenggu.http.response.EventResponse;
import com.haenggu.repository.EventImageRepository;
import com.haenggu.repository.EventRepository;
import io.swagger.models.auth.In;
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

    private static final Integer favorite = 10;

    @Autowired
    public EventService(EventRepository eventRepository, EventImageRepository eventImageRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public Page<EventResponse> findEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(event -> makeEventResponse(event, favorite));
    }

    public Page<EventResponse> findEvents(CategoryType categoryType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByCategory(categoryType, pageable);
        return events.map(event -> makeEventResponse(event, favorite));
    }

    public Page<EventResponse> findEvents(RegionType regionType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByRegion(regionType, pageable);
        return events.map(event -> makeEventResponse(event, favorite));
    }

    public Page<EventResponse> findEvents(CategoryType categoryType,RegionType regionType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByCategoryAndRegion(categoryType, regionType, pageable);
        return events.map(event -> makeEventResponse(event, favorite));
    }

    public List<String> getTagList() {
        return eventRepository.getTagList();
    }

    public EventResponse findById(UUID idx) {
        Event event = eventRepository.getEventByEventId(idx);
        return makeEventResponse(event, favorite);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
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
        if(!Objects.isNull(event.getReservationStartedDate())) {
            persistEvent.setReservationStartedDate(event.getReservationStartedDate());
        }
        if(!Objects.isNull(event.getReservationEndedDate())) {
            persistEvent.setReservationEndedDate(event.getReservationEndedDate());
        }
        if(!Objects.isNull(event.getTime())) {
            persistEvent.setTime((event.getTime()));
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

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

            return eventImageRepository.save(eventImage);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public EventImage loadFileAsByte(UUID imageId) {
        return eventImageRepository.getEventImageByImageId(imageId);
    }

    public EventResponse makeEventResponse(Event event, Integer favorite) {
        EventResponse response = EventResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .favorite(favorite)
                .startedDate(event.getStartedDate())
                .endedDate(event.getEndedDate())
                .reservationStartedDate(event.getReservationStartedDate())
                .reservationEndedDate(event.getReservationEndedDate())
                .time(event.getTime())
                .eventLocation(event.getEventLocation())
                .category(event.getCategory())
                .region(event.getRegion())
                .tag(event.getTag())
                .imageUrl(getImageUri(event.getImage()))
                .build();

        response.addTag(event.getRegion().getValue());

        return response;
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
