package com.haenggu.repository;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.enums.CategoryType;
import com.haenggu.domain.enums.RegionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Event getEventByEventId(@Param("event_id") UUID idx);
    @Query(value = "SELECT DISTINCT e.tag FROM event_tag e", nativeQuery = true)
    List<String> getTagList();
    Optional<Event> findEventByEventId(@Param("event_id") UUID idx);
    Page<Event> findEventByCategory(CategoryType category, Pageable pageable);
    Page<Event> findEventByRegion(RegionType region, Pageable pageable);
    Page<Event> findEventByCategoryAndRegion(CategoryType categoryType, RegionType regionType, Pageable pageable);
    @Query(
            value = "SELECT * FROM (event JOIN event_like el ON (event.event_id = el.event_id AND el.user_id = :idx))",
            countQuery = "SELECT count(*) FROM event JOIN event_like el ON (event.event_id = el.event_id AND el.user_id = :idx)",
            nativeQuery = true
    )
    Page<Event> findEventByUser(@Param("idx") UUID user_id, Pageable pageable);
    @Transactional
    void deleteEventByEventId(@Param("event_id") UUID idx);
}