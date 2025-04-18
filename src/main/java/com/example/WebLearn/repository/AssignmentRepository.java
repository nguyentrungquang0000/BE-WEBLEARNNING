package com.example.WebLearn.repository;

import com.example.WebLearn.entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Page<Assignment> findByTitleContainingIgnoreCaseAndClassroom_Id(String title, String classId, Pageable pageable);

}
