package com.example.WebLearn.service;

import com.example.WebLearn.entity.QuizSubmit;
import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnswerDetailService {
    Pair<Long, Long> saveAnswerDetail(List<AnswerDetailRequest> answerDetailRequests, QuizSubmit quizSubmit);
    ResponseEntity<Response<Object>> getAnswerDetail(String classId, Long quizTestId, Long submitId);
}
