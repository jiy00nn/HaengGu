package com.haenggu.repository;

import com.haenggu.domain.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<School, UUID>  {
    List<School> findBySchoolNameContaining(String schoolName);
}
