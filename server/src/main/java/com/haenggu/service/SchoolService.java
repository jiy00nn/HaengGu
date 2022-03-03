package com.haenggu.service;

import com.haenggu.domain.entity.School;
import com.haenggu.http.response.SchoolNameResponse;
import com.haenggu.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<School> findSchoolBySchoolName(String name, Pageable pageable) {
        return this.schoolRepository.findBySchoolNameContaining(name, pageable);
    }

    public Page<School> findSchoolBySchoolNameAndMajorName(String schoolName, String majorName, Pageable pageable) {
        return this.schoolRepository.findBySchoolNameContainingAndDeptNameContaining(schoolName, majorName, pageable);
    }

    public Page<SchoolNameResponse> findSchoolNames(Pageable pageable) {
        return this.schoolRepository.findSchoolName(pageable);
    }

    public Page<SchoolNameResponse> findSchoolNamesByString(String name, Pageable pageable) {
        return this.schoolRepository.findSchoolNameContaining(name, pageable);
    }

}
