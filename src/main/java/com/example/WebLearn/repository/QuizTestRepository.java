package com.example.WebLearn.repository;

import com.example.WebLearn.entity.QuizTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizTestRepository extends JpaRepository<QuizTest, Long> {
    Page<QuizTest> findByNameContainingIgnoreCaseAndClassroom_Id(String name, String classroomId, Pageable pageable);
}
