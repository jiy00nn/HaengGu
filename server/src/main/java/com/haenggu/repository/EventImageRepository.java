package com.haenggu.repository;

import com.haenggu.domain.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
}
