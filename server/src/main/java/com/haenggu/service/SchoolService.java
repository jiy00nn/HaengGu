package com.haenggu.service;

import com.haenggu.domain.entity.School;
import com.haenggu.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public School findSchoolById(UUID id) {
        return this.schoolRepository.getById(id);
    }

    public List<School> findSchoolBySchoolNameAndMajorName(String schoolName) {
        return this.schoolRepository.findBySchoolNameContaining(schoolName);
    }
}
