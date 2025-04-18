package com.example.WebLearn.repository;

import com.example.WebLearn.entity.QuizSubmit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSubmitRepository extends JpaRepository<QuizSubmit, Long> {
    Page<QuizSubmit> findByStudent_NameContainingIgnoreCaseAndQuizTest_Id(String keyword,long quizTestId, Pageable pageable);
}
