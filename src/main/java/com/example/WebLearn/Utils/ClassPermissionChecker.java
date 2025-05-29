package com.example.WebLearn.Utils;

import com.example.WebLearn.entity.ClassMember;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.enumm.StatusClassMember;
import com.example.WebLearn.repository.ClassMemberRepository;
import com.example.WebLearn.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("classPermission")
@RequiredArgsConstructor
public class ClassPermissionChecker {
    public final ClassroomRepository classroomRepository;
    public final ClassMemberRepository classMemberRepository;

    public boolean hasAccess(String classId) {
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
}
