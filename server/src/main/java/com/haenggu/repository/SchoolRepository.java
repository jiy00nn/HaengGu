package com.haenggu.repository;

import com.haenggu.domain.entity.School;
import com.haenggu.http.response.SchoolNameResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<School, UUID>  {
    Page<School> findBySchoolNameContaining(String schoolName, Pageable pageable);
    Page<School> findBySchoolNameContainingAndDeptNameContaining(String schoolName, String deptName, Pageable pageable);
    @Query(value = "SELECT DISTINCT new com.haenggu.http.response.SchoolNameResponse(s.schoolName) FROM School s",
           countQuery = "SELECT count(schoolName) FROM School")
    Page<SchoolNameResponse> findSchoolName(Pageable pageable);
    @Query(value = "SELECT DISTINCT new com.haenggu.http.response.SchoolNameResponse(s.schoolName) FROM School s WHERE s.schoolName LIKE CONCAT('%', :schoolName, '%')" ,
            countQuery = "SELECT count(schoolName) FROM School")
    Page<SchoolNameResponse> findSchoolNameContaining(@Param("schoolName") String schoolName, Pageable pageable);
}
