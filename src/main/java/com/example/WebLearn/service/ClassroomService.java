package com.example.WebLearn.service;

import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClassroomService {
    ResponseEntity<Response<Object>> createClassroom(String email, String name, String avatarUrl);
    ResponseEntity<Response<Object>> deleteClassroom(String email, String classroomId);
    ResponseEntity<Response<Object>> getClassroom(String classroomId);
    ResponseEntity<Response<Object>> getDashboard(String classId);
}
