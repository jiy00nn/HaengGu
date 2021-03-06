package com.haenggu.http.response.event;

import com.haenggu.controller.EventController;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class EventListResponse {
    private final PagedModel<EventResponse> resources;

    public EventListResponse(Pageable pageable, Page<EventResponse> page) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), page.getNumber(), page.getTotalElements());
        this.resources = PagedModel.of(page.getContent(), pageMetadata);
        this.resources.add(linkTo(methodOn(EventController.class).getEvents(pageable, null, null)).withSelfRel());
    }
}
