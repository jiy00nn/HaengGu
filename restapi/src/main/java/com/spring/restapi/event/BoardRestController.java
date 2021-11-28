package com.spring.restapi.event;

import com.spring.restapi.domain.Board;
import com.spring.restapi.repository.BoardRepostitory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {
    private BoardRepostitory boardRepository;

    public BoardRestController(BoardRepostitory boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("/boards")
    public @ResponseBody
    CollectionModel<Board> simpleBoard(@PageableDefault Pageable pageable) {
        Page<Board> boardList = boardRepository.findAll(pageable);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(), boardList.getNumber(), boardList.getTotalElements());
        PagedModel<Board> resources = new PagedModel<>(boardList.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(BoardRestController.class).simpleBoard(pageable)).withSelfRel());
        return resources;
    }
}
