package com.haenggu.repository;

import com.haenggu.domain.entity.Board;
import com.haenggu.domain.entity.BoardLike;
import com.haenggu.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, UUID> {
    Optional<BoardLike> findBoardLikeByUserAndBoard(Users user, Board board);
}
