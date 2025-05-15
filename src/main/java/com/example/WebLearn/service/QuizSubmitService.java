package com.example.WebLearn.service;

import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizSubmitService {
    ResponseEntity<Response<Object>> saveSubmit(List<AnswerDetailRequest> answerDetailRequests, Long quizTestId, String email);
    ResponseEntity<Response<Object>> getSubmit(String classId, Long quizTestId, SearchRequest searchRequest);
    ResponseEntity<Response<Object>> getResultQuiz(Long quizSubmitId);

}
