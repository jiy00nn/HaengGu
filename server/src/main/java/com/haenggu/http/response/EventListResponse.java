package com.haenggu.http.response;


import com.haenggu.controller.EventController;

import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
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
        this.resources.add(linkTo(methodOn(EventController.class).getEvents(pageable, CategoryType.ETC, RegionType.SEOUL)).withSelfRel());
    }
}
