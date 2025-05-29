package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.Teacher;
import com.example.WebLearn.enumm.StatusClassMember;
import com.example.WebLearn.model.dto.ClassByUserDTO;
import com.example.WebLearn.model.dto.ClassDashboardDTO;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.TeacherRepository;
import com.example.WebLearn.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Override
    public ResponseEntity<Response<Object>> createClassroom(String email, String name, String avatarUrl) {
        Teacher teacher = teacherRepository.findByEmail(email).orElse(null);
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setAvatarUrl(avatarUrl);
        classroom.setTeacher(teacher);
        classroomRepository.save(classroom);
        return ResponseEntity.ok(new Response<>(200, "Tạo thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> deleteClassroom(String email, String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElse(null);
        if(classroom == null || !classroom.getTeacher().getEmail().equals(email)) {
            return ResponseEntity.status(400).body(new Response<>(400, "Lỗi quyền", null));
        }
        classroomRepository.deleteById(classroomId);
        return ResponseEntity.ok(new Response<>(200, "Xoá thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getClassroom(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        ClassByUserDTO classByUserDTO = new ClassByUserDTO();
        classByUserDTO.setClassName(classroom.getName());
        classByUserDTO.setClassId(classroom.getId());
        return ResponseEntity.ok(new Response<>(200, "ok", classByUserDTO));
    }

    @Override
    public ResponseEntity<Response<Object>> getDashboard(String classId) {
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        ClassDashboardDTO classDashboardDTO = new ClassDashboardDTO(
                classroom.getLectures().size(),
                classroom.getAssignments().size(),
                classroom.getClassMembers().stream()
                        .filter(classMember -> classMember.getStatusClassMember().equals(StatusClassMember.APPROVED))
                        .count(),
                classroom.getQuizTests().size(),
                classroom.getName(),
                classroom.getTeacher().getName(),
                classroom.getId()
        );
        return ResponseEntity.ok(new Response<>(200, "ok", classDashboardDTO));
    }
}
