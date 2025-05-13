package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.ScoreRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.AssignmentSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AssignmentSubmitController {
    @Autowired
    private AssignmentSubmitService assignmentSubmitService;

    @PostMapping("/class/{classId}/assignment/{assId}")
    public ResponseEntity<Response<Object>> submitAssignment(@PathVariable String classId,
                                                             @PathVariable Long assId,
                                                             @RequestPart("multipartFile") MultipartFile file
    ) {
        return assignmentSubmitService.saveSubmit(classId, assId, file);
    }

    @PutMapping("/class/{classId}/assignment/{assId}/s/{submitId}")
    public ResponseEntity<Response<Object>> scoredSubmit(@PathVariable String classId,
                                                         @PathVariable Long assId,
                                                         @PathVariable Long submitId,
                                                         @RequestBody ScoreRequest scoreRequest
    ) {
        return assignmentSubmitService.scoreSubmit(classId, assId, submitId, scoreRequest.getScore());
    }

    @DeleteMapping("/class/{classId}/assignment/{assId}/d/{submitId}")
    public ResponseEntity<Response<Object>> deleteSubmit(@PathVariable String classId,
                                                         @PathVariable Long assId,
                                                         @PathVariable Long submitId) {
        return assignmentSubmitService.deleteSubmit(classId, assId, submitId);
    }

    @GetMapping("/class/{classId}/assignment/{assId}")
    public ResponseEntity<Response<Object>> getAssignmentSubmit(@PathVariable String classId,
                                                                @PathVariable Long assId,
                                                                @ModelAttribute SearchRequest searchRequest) {
        return assignmentSubmitService.getAssignmentSubmit(classId, assId, searchRequest);
    }

    @GetMapping("/submit/{assId}")
    public ResponseEntity<Response<Object>> getSubmitStu(@PathVariable Long assId) {
        return assignmentSubmitService.getSubmitStu(assId);
    }

    //chua lm xong
    @GetMapping("/class/{classId}/quiz/{quizId}/result")
    public ResponseEntity<Response<Object>> getResultQuiz(@PathVariable String classId, @PathVariable Long quizId) {
        return null;
    }
}
