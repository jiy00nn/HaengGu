package com.haenggu.repository;

import com.haenggu.domain.entity.Board;
import com.haenggu.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findBoardByBoardIdAndUser(UUID id, Users user);
    @Query(
            value = "SELECT * FROM (board JOIN board_like bl ON (board.board_id = bl.board_id AND bl.user_id = :idx))",
            countQuery = "SELECT count(*) FROM (board JOIN board_like bl ON (board.board_id = bl.board_id AND bl.user_id = :idx))",
            nativeQuery = true
    )
    Page<Board> findBoardByUser(@Param("idx") UUID userId, Pageable pageable);
}
