package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.QuizSubmitRedis.QuizSubmitRedisService;
import com.example.WebLearn.service.QuizSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class QuizSubmitController {
    @Autowired
    private QuizSubmitService quizSubmitService;
    @Autowired
    private QuizSubmitRedisService quizSubmitRedisService;
//    @PostMapping("/quiztest/{quizTestId}")
//    public ResponseEntity<Response<Object>> submitQuiz(@PathVariable Long quizTestId,
//                                                       @RequestBody List<AnswerDetailRequest> answerDetailRequests) {
//        return quizSubmitService.saveSubmit(answerDetailRequests, quizTestId);
//    }

    @GetMapping("class/{classId}/test/{quizTestId}/submit")
    public ResponseEntity<Response<Object>> getQuizSubmit(@PathVariable String classId,
                                                          @PathVariable Long quizTestId,
                                                          @ModelAttribute SearchRequest searchRequest
                                                          ) {
        return quizSubmitService.getSubmit(classId, quizTestId, searchRequest);
    }

    @PostMapping("/class/{classId}/quiz/{quizId}/start")
    public ResponseEntity<Void> startQuiz(@PathVariable String classId,
                                          @PathVariable Long quizId){
        return quizSubmitRedisService.saveQuizStart(classId, quizId);
    }

    @PutMapping("/class/{classId}/quiz/{quizId}/update_answer")
    public ResponseEntity<Void> updateAnswer(@PathVariable String classId,
                                             @PathVariable Long quizId,
                                             @RequestBody AnswerDetailRequest answerDetailRequest
                                             ){
        return quizSubmitRedisService.updateAnswer(classId, quizId, answerDetailRequest);
    }
    // Lay trang thai bai lam
    @GetMapping("/get_status_quiz/{quizId}")
    public ResponseEntity<Response<Object>> getQuizStatus(@PathVariable Long quizId){
        return quizSubmitRedisService.getQuizStatus(quizId);
    }

    @PostMapping("/submit_quiz/{quizId}")
    public ResponseEntity<Response<Object>> submitQuiz(@PathVariable Long quizId){
        return quizSubmitRedisService.submitQuiz(quizId, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/quizzing/{quizId}")
    public ResponseEntity<Response<Object>> getQuizzing(@PathVariable Long quizId){
        return quizSubmitRedisService.getQuizzing(quizId);
    }

    @GetMapping("/quizsubmit/{quizSubmitId}")
    public ResponseEntity<Response<Object>> getResultQuiz(@PathVariable Long quizSubmitId){
        return quizSubmitService.getResultQuiz(quizSubmitId);
    }

}
