package com.example.WebLearn.repository;

import com.example.WebLearn.entity.ClassMember;
import com.example.WebLearn.enumm.StatusClassMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassMemberRepository extends JpaRepository<ClassMember, Long> {
    Page<ClassMember> findByClassroom_NameContainingIgnoreCaseAndStudent_IdAndStatusClassMember(String keyword, Long studentId, StatusClassMember status, Pageable pageable);
    Page<ClassMember> findByStudent_NameContainingIgnoreCaseAndStatusClassMemberAndClassroom_Id(String keyword, StatusClassMember status, String classId, Pageable pageable);
    List<ClassMember> findByClassroom_IdAndStatusClassMember(String classId, StatusClassMember status);
    ClassMember findByClassroom_IdAndStudent_Email(String classId, String email);
    ClassMember findByClassroom_IdAndStudent_EmailAndStatusClassMember(String classId, String email, StatusClassMember status);
}
