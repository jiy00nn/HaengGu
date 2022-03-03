package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.controller.UserController;
import com.haenggu.domain.entity.School;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SchoolListResponse {
    private final PagedModel<School> resources;

    public SchoolListResponse(Pageable pageable, Page<School> page) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), page.getNumber(), page.getTotalElements());
        this.resources = PagedModel.of(page.getContent(), pageMetadata);
        this.resources.add(linkTo(methodOn(UserController.class).getSchoolName(pageable, null)).withSelfRel());
    }
}
