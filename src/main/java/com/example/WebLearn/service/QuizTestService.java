package com.example.WebLearn.service;

import com.example.WebLearn.model.request.QuizTestRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;

public interface QuizTestService {
    ResponseEntity<Response<Object>> createQuizzTest(String classId, QuizTestRequest quizTestRequest);
    ResponseEntity<Response<Object>> getQuizTest(String classId, SearchRequest searchRequest);
}
