package com.example.WebLearn.repository;

import com.example.WebLearn.entity.AssignmentSubmit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentSubmitRepository extends JpaRepository<AssignmentSubmit, Long> {
    Page<AssignmentSubmit> findByStudent_NameContainingIgnoreCaseAndAssignment_Id(String keyword, Long assId, Pageable pageable);
    void deleteByAssignment_Id(Long id);
    AssignmentSubmit findByAssignment_IdAndStudent_Email(Long id, String email);
}
