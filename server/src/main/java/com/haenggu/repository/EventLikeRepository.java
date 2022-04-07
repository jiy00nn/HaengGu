package com.haenggu.repository;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.EventLike;
import com.haenggu.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, UUID> {
    Optional<EventLike> findEventLikeByUserAndEvent(Users user, Event event);
    @Query("SELECT COUNT(e) FROM EventLike e WHERE e.event.eventId=?1")
    long countEventLikeByUser(UUID id);
}
