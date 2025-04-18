package com.example.WebLearn.repository;

import com.example.WebLearn.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuizTest_Id(Long id);
}
