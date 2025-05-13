package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.ClassroomRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;
    @PostMapping("/create-classroom")
    public ResponseEntity<Response<Object>> createClassroom(@RequestBody ClassroomRequest classroomRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return classroomService.createClassroom(email, classroomRequest.getName(), classroomRequest.getAvatarUrl());
    }

    @PutMapping("/update-classroom")
    public ResponseEntity<Response<Object>> updateClassroom(@RequestBody ClassroomRequest classroomRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return classroomService.createClassroom(email, classroomRequest.getName(), classroomRequest.getAvatarUrl());
    }

    @DeleteMapping("/delete-classroom/{classId}")
    public ResponseEntity<Response<Object>> deleteClassroom(@PathVariable String classId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return classroomService.deleteClassroom(email, classId);
    }
    @GetMapping("/class/{classId}")
    public ResponseEntity<Response<Object>> getClassroom(@PathVariable String classId) {
        return classroomService.getClassroom(classId);
    }
}
