package com.haenggu.repository;

import com.haenggu.domain.entity.Board;
import com.haenggu.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findBoardByBoardIdAndUser(UUID id, Users user);
    Board getByBoardIdAndUser(UUID id, Users users);
}
