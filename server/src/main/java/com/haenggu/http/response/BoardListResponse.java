package com.haenggu.http.response;

import com.haenggu.controller.BoardController;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class BoardListResponse {
    private final PagedModel<BoardResponse> resources;

    public BoardListResponse(Pageable pageable, Page<BoardResponse> page) {
        PagedModel.PageMetadata pageMetadata =
                new PagedModel.PageMetadata(pageable.getPageSize(), page.getNumber(), page.getTotalElements());
        this.resources = PagedModel.of(page.getContent(), pageMetadata);
        this.resources.add(linkTo(methodOn(BoardController.class).getBoards(pageable)).withSelfRel());
    }
}
