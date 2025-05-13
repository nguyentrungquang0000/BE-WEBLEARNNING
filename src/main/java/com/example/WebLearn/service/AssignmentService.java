package com.example.WebLearn.service;

import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import java.io.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface AssignmentService {
    ResponseEntity<Response<Object>> createAssignment(String classId, String title, String description, Date dueDate, MultipartFile file);
    ResponseEntity<Response<Object>> deleteAssignment(String classId, Long assId);
    ResponseEntity<Response<Object>> getAssignments(String classId, SearchRequest searchRequest);
    ResponseEntity<Response<Object>> updateAssignment(String classId, Long assId, String title, String description, Date dueDate, MultipartFile file, String change);
    ResponseEntity<Response<Object>> getAssignmentDetail(Long assId);
}
