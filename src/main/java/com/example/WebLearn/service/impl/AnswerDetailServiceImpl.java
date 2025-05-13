package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.AnswerDetail;
import com.example.WebLearn.entity.QuizSubmit;
import com.example.WebLearn.model.dto.AnswerDetailDTO;
import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.AnswerDetailRepository;
import com.example.WebLearn.repository.QuestionRepository;
import com.example.WebLearn.service.AnswerDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerDetailServiceImpl implements AnswerDetailService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerDetailRepository answerDetailRepository;

    @Transactional
    @Override
    public Pair<Long, Integer> saveAnswerDetail(List<AnswerDetailRequest> answerDetailRequests, QuizSubmit quizSubmit) {
        for(AnswerDetailRequest answerDetailRequest : answerDetailRequests){
            AnswerDetail answerDetail = new AnswerDetail();
            answerDetail.setQuestion(questionRepository.findById(answerDetailRequest.getQuestionId()).orElse(null));
            answerDetail.setQuizSubmit(quizSubmit);
            answerDetail.setAnswer(answerDetailRequest.getAnswer());
            answerDetailRepository.save(answerDetail);
        }
        //kiểm tra
        answerDetailRepository.updateStatusAnswer(quizSubmit.getId());
        //chấm điểm
        Pair<Long, Integer> correct = answerDetailRepository.correctAnswers(quizSubmit.getId());
        return correct;
    }

    @Override
    public ResponseEntity<Response<Object>> getAnswerDetail(String classId, Long quizTestId, Long submitId) {
        List<AnswerDetail> answerDetails = answerDetailRepository.findByQuizSubmit_Id(submitId);
        List<AnswerDetailDTO> answerDetailDTOs = answerDetails.stream().map(
                answerDetail -> new AnswerDetailDTO(
                       answerDetail.getQuestion().getId(),
                        answerDetail.getQuestion().getTitle(),
                        answerDetail.getAnswer(),
                        answerDetail.getStatusAnswer(),
                        answerDetail.getQuestion().getResult()
                )
        ).toList();
        return ResponseEntity.ok(new Response<>(200, "ok", answerDetailDTOs));
    }
}
