package com.example.WebLearn.service.QuizSubmitRedis;

import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface QuizSubmitRedisService {
    //Lưu trang thái ban đầu khi bắt đầu làm
    ResponseEntity<Void> saveQuizStart(String classId, Long quizId);
    //Cập nhật Answer trong Redis khi có thay đổi
    ResponseEntity<Void> updateAnswer(String classId, Long quizId, AnswerDetailRequest answerDetailRequest);
    //Lấy trạng thái bài thi
    ResponseEntity<Response<Object>> getQuizStatus(Long quizId);
    //Lưu khi submit or hết h
    ResponseEntity<Response<Object>> submitQuiz(Long quizId, String email);
    //Lấy thông tin để làm quiz
    ResponseEntity<Response<Object>> getQuizzing(Long quizId);
}
