package com.example.WebLearn.repository;

import com.example.WebLearn.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {
    Page<Classroom> findByNameContainingIgnoreCaseAndTeacher_Email(String keyword, String email, Pageable pageable);
}
