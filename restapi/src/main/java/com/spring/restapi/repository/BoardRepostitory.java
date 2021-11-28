package com.spring.restapi.repository;

import com.spring.restapi.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BoardRepostitory extends JpaRepository<Board, Long> {

}
