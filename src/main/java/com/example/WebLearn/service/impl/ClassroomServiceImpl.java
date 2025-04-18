package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.Teacher;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.TeacherRepository;
import com.example.WebLearn.service.ClassroomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
