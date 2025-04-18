package com.example.WebLearn.service;

import com.example.WebLearn.entity.Question;
import com.example.WebLearn.model.request.QuestionRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<Response<Object>> saveQuestion(Long quizTestId, List<QuestionRequest> questionRequest);
    ResponseEntity<Response<Object>> getQuestion(String classId, Long quizTestId);
}
