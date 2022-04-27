package com.haenggu.service;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventImage;
import com.haenggu.domain.entity.EventLike;
import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import com.haenggu.exception.FileStorageException;
import com.haenggu.http.response.BoardSimpleResponse;
import com.haenggu.http.response.event.EventDetailResponse;
import com.haenggu.http.response.event.EventResponse;
import com.haenggu.repository.EventImageRepository;
import com.haenggu.repository.EventLikeRepository;
import com.haenggu.repository.EventRepository;
import com.haenggu.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventLikeRepository eventLikeRepository;
    private final EventImageRepository eventImageRepository;

    @Autowired
    public EventService(UserRepository userRepository, EventRepository eventRepository, EventLikeRepository eventLikeRepository, EventImageRepository eventImageRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventLikeRepository = eventLikeRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public Page<EventResponse> findEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::makeEventResponse);
    }

    public Page<EventResponse> findEvents(CategoryType categoryType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByCategory(categoryType, pageable);
        return events.map(this::makeEventResponse);
    }

    public Page<EventResponse> findEvents(RegionType regionType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByRegion(regionType, pageable);
        return events.map(this::makeEventResponse);
    }

    public Page<EventResponse> findEvents(CategoryType categoryType,RegionType regionType, Pageable pageable) {
        Page<Event> events = eventRepository.findEventByCategoryAndRegion(categoryType, regionType, pageable);
        return events.map(this::makeEventResponse);
    }

    public Page<EventResponse> findEventsLike(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.getById(UUID.fromString(authentication.getPrincipal().toString()));
        Page<Event> events = eventRepository.findEventByUser(user.getUserId(), pageable);
        return events.map(this::makeEventResponse);
    }

    public List<String> getTagList() {
        return eventRepository.getTagList();
    }

    public EventDetailResponse findById(UUID idx) {
        Event event = eventRepository.getEventByEventId(idx);
        return makeEventDetailResponse(event);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void saveEventLike(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.getById(UUID.fromString(authentication.getPrincipal().toString()));
        eventLikeRepository.save(EventLike.builder().event(eventRepository.getById(id)).user(user).build());
    }

    public void deleteEventLike(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.getById(UUID.fromString(authentication.getPrincipal().toString()));
        eventLikeRepository.delete(eventLikeRepository.findEventLikeByUserAndEvent(user, eventRepository.getById(id)).get());
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

    public Long countEventFavorite(UUID eventId) {
        return eventLikeRepository.countEventLikeByUser(eventId);
    }

    public EventResponse makeEventResponse(Event event) {
        return EventResponse.builder()
                .event(event)
                .favorite(countEventFavorite(event.getEventId()))
                .build();
    }

    public EventDetailResponse makeEventDetailResponse(Event event) {
        return new EventDetailResponse(event);
    }
}
