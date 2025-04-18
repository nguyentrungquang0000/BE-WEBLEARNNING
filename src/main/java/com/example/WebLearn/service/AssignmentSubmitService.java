package com.example.WebLearn.service;

import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentSubmitService {
    ResponseEntity<Response<Object>> saveAssignment(String classId, Long assId, MultipartFile file);
    ResponseEntity<Response<Object>> scoreSubmit(String classId, Long assId, Long submitId, float score);
    ResponseEntity<Response<Object>> deleteSubmit(String classId, Long assId, Long submitId);
    ResponseEntity<Response<Object>> getAssignmentSubmit(String classId, Long assId, SearchRequest searchRequest);
}
