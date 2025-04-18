package com.example.WebLearn.service;

import com.example.WebLearn.entity.AnswerDetail;
import com.example.WebLearn.entity.QuizSubmit;
import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.response.Response;
import com.google.api.services.drive.Drive;
import jakarta.persistence.Tuple;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnswerDetailService {
    Tuple saveAnswerDetail(List<AnswerDetailRequest> answerDetailRequests, QuizSubmit quizSubmit);
    ResponseEntity<Response<Object>> getAnswerDetail(String classId, Long quizTestId, Long submitId);
}
