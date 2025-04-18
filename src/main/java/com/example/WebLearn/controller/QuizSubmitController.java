package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.QuizSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizSubmitController {
    @Autowired
    private QuizSubmitService quizSubmitService;
    @PostMapping("/quiztest/{quizTestId}")
    public ResponseEntity<Response<Object>> submitQuiz(@PathVariable Long quizTestId,
                                                       @RequestBody List<AnswerDetailRequest> answerDetailRequests) {
        return quizSubmitService.saveSubmit(answerDetailRequests, quizTestId);
    }

    @GetMapping("class/{classId}/test/{quizTestId}/submit")
    public ResponseEntity<Response<Object>> getQuizSubmit(@PathVariable String classId,
                                                          @PathVariable Long quizTestId,
                                                          @ModelAttribute SearchRequest searchRequest
                                                          ) {
        return quizSubmitService.getSubmit(classId, quizTestId, searchRequest);
    }
}
