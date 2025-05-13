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
                 @RequestParam(value = "title") String title,
                 @RequestParam(value = "description", required = false) String description,
                 @RequestParam(value = "dueDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dueDate,
                 @RequestPart(value = "multipartFile", required = false) MultipartFile file
                ) throws GeneralSecurityException, IOException {
        return assignmentService.createAssignment(classId, title, description, dueDate, file);
    }

    @DeleteMapping("/class/{classId}/assignment/{assId}")
    public ResponseEntity<Response<Object>> deleteAssignment(@PathVariable String classId, @PathVariable Long assId){
        return assignmentService.deleteAssignment(classId, assId);
    }

    @PutMapping("/class/{classId}/assignment/{assId}")
    public ResponseEntity<Response<Object>> updateAssignment(@PathVariable String classId,
                                                             @PathVariable Long assId,
                                                             @RequestParam(value = "title") String title,
                                                             @RequestParam(value = "description", required = false) String description,
                                                             @RequestParam(value = "dueDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dueDate,
                                                             @RequestPart(value = "multipartFile", required = false) MultipartFile file,
                                                             @RequestParam(value = "change", defaultValue = "false", required = false) String change){
        return assignmentService.updateAssignment(classId, assId, title, description, dueDate, file, change);
    }

    @GetMapping("/class/{classId}/assignment")
    public ResponseEntity<Response<Object>> getAssignments(@PathVariable String classId,
                                                           @ModelAttribute SearchRequest searchRequest
                                                           ) {

        return assignmentService.getAssignments(classId, searchRequest);
    }

    @GetMapping("/assignment/{assId}")
    public ResponseEntity<Response<Object>> getAssignmentDetail(@PathVariable Long assId){
        return assignmentService.getAssignmentDetail(assId);
    }
}
