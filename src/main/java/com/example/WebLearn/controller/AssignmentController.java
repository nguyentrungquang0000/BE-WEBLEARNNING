package com.example.WebLearn.controller;

import com.example.WebLearn.entity.Assignment;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.AssignmentService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/class/{classId}/assignment")
    public ResponseEntity<Response<Object>> createAssignment(
                 @PathVariable String classId,
                 @RequestParam("title") String title,
                 @RequestParam("description") String description,
                 @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dueDate,
                 @RequestPart("multipartFile") MultipartFile file
                ) throws GeneralSecurityException, IOException {
        return assignmentService.createAssignment(classId, title, description, dueDate, file);
    }

    @DeleteMapping("/class/{classId}/assignment/{assId}")
    public ResponseEntity<Response<Object>> deleteAssignment(@PathVariable String classId, @PathVariable Long assId){
        return assignmentService.deleteAssignment(classId, assId);
    }

    @GetMapping("class/{classId}/assignment")
    public ResponseEntity<Response<Object>> getAssignments(@PathVariable String classId,
                                                           @ModelAttribute SearchRequest searchRequest
                                                           ) {

        return assignmentService.getAssignments(classId, searchRequest);
    }
}
