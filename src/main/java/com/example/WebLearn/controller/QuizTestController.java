package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.QuizTestRequest;
import com.example.WebLearn.model.request.QuizUpdateRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.QuestionService;
import com.example.WebLearn.service.QuizTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizTestController {
    @Autowired
    private QuizTestService quizTestService;
    @PostMapping("class/{classId}/test")
    public ResponseEntity<Response<Object>> createQuizTest(@PathVariable("classId") String classId,
                                                           @RequestBody QuizTestRequest quizTestRequest) {
        return quizTestService.createQuizzTest(classId, quizTestRequest);
    }
    @PreAuthorize("@classPermission.hasAccess(#classId)")
    @GetMapping("/class/{classId}/test")
    public ResponseEntity<Response<Object>> getQuizTest(@PathVariable("classId") String classId,
                                                        @ModelAttribute SearchRequest searchRequest
    ) {
        return quizTestService.getQuizTest(classId, searchRequest);
    }
    @PreAuthorize("@classPermission.hasAccess(#classId)")
    @GetMapping("/class/{classId}/quiz/{quizId}/detail")
    public ResponseEntity<Response<Object>> getQuizDetail(@PathVariable String classId,
                                                          @PathVariable Long quizId) {
        return quizTestService.getQuizDetail(classId, quizId);
    }

    @PutMapping("/quiz/{quizId}/update")
    public ResponseEntity<Response<Object>> updateQuizInfo(@PathVariable Long quizId, @RequestBody QuizUpdateRequest quizUpdateRequest){
        return quizTestService.updateQuizInfo(quizId, quizUpdateRequest);
    }
}
