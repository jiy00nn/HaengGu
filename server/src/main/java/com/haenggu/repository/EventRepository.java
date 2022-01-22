package com.haenggu.repository;

import com.haenggu.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {
    @Override void deleteById(Long aLong);
}