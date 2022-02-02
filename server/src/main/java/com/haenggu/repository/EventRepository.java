package com.haenggu.repository;

import com.haenggu.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event getEventByEventId(@Param("event_id") UUID idx);
    Optional<Event> findEventByEventId(@Param("event_id") UUID idx);
    void deleteEventByEventId(@Param("event_id") UUID idx);
    @Override void deleteById(Long aLong);
}