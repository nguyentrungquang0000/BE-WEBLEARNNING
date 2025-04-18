package com.example.WebLearn.Utils;

import com.example.WebLearn.entity.*;
import com.example.WebLearn.enumm.StatusClassMember;
import com.example.WebLearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationCheckUtil {
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private ClassMemberRepository classMemberRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private QuizTestRepository quizTestRepository;
    public boolean checkUserAccessToClassroom(String classId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            Classroom classroom = classroomRepository.findById(classId).orElse(null);
            return classroom != null && classroom.getTeacher().getEmail().equals(email);
        }

        if (isUser) {
            ClassMember classMember = classMemberRepository.findByClassroom_IdAndStudent_Email(classId, email);
            return classMember != null && classMember.getStatusClassMember() == StatusClassMember.APPROVED;
        }

        return false; // Nếu không phải admin hoặc user
    }

    public boolean checkAssignmentInClassroom(String classId, Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        return assignment.getClassroom().getId().equals(classId);
    }

    public boolean checkLectureInClassroom(String classId, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        return lecture.getClassroom().getId().equals(classId);
    }

    public boolean checkTestInClassroom(String classId, Long testId) {
        QuizTest quizTest = quizTestRepository.findById(testId).orElseThrow();
        return quizTest.getClassroom().getId().equals(classId);
    }

}

