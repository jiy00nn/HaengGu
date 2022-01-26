package com.haenggu.repository;

import com.haenggu.domain.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    EventImage getEventImageByImageId(@Param("image_id") UUID idx);
}
