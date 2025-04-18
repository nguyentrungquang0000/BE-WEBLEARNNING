package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.Question;
import com.example.WebLearn.entity.QuizTest;
import com.example.WebLearn.model.request.QuestionRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.QuestionRepository;
import com.example.WebLearn.repository.QuizTestRepository;
import com.example.WebLearn.service.QuestionService;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QuizTestRepository quizTestRepository;

    @Transactional
    @Override
    public ResponseEntity<Response<Object>> saveQuestion(Long quizTestId,List<QuestionRequest> questionRequest) {
        QuizTest quizTest = quizTestRepository.findById(quizTestId).orElseThrow();
        for (QuestionRequest question : questionRequest) {
            Question questionEntity = questionRepository.save(modelMapper.map(question, Question.class));
            questionEntity.setQuizTest(quizTest);
            questionRepository.save(questionEntity);
        }
        return ResponseEntity.ok(new Response<>(200, "ok", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getQuestion(String classId, Long quizTestId) {
        List<Question>  questions = questionRepository.findByQuizTest_Id(quizTestId);

        return ResponseEntity.ok(new Response<>(200, "ok", questions));
    }
}
