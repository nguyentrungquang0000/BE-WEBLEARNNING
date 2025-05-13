package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.PageToDTOUltil;
import com.example.WebLearn.entity.QuizSubmit;
import com.example.WebLearn.entity.QuizTest;
import com.example.WebLearn.entity.Student;
import com.example.WebLearn.model.dto.QuizSubmitDTO;
import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.QuizSubmitRepository;
import com.example.WebLearn.repository.QuizTestRepository;
import com.example.WebLearn.repository.StudentRepository;
import com.example.WebLearn.service.AnswerDetailService;
import com.example.WebLearn.service.QuizSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class QuizSubmitServiceImpl implements QuizSubmitService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuizSubmitRepository quizSubmitRepository;
    @Autowired
    private QuizTestRepository quizTestRepository;
    @Autowired
    private AnswerDetailService answerDetailService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Override
    @Transactional
    public ResponseEntity<Response<Object>> saveSubmit(List<AnswerDetailRequest> answerDetailRequests, Long quizTestId, String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow();
        QuizSubmit quizSubmit = new QuizSubmit();
        QuizTest quizTest = quizTestRepository.findById(quizTestId).orElseThrow();
        quizSubmit.setQuizTest(quizTest);
        quizSubmit.setStudent(student);
        Pair<Long, Integer> correct = answerDetailService.saveAnswerDetail(answerDetailRequests, quizSubmitRepository.save(quizSubmit));
        if (correct == null) {
            correct = Pair.of(quizSubmit.getId(), 0);
        }
        quizSubmit = quizSubmitRepository.findById(correct.getFirst()).orElseThrow();
        quizSubmit.setScore((float) correct.getSecond() / (float) quizTest.getCountQuestion() * 10);
        quizSubmitRepository.save(quizSubmit);
        return ResponseEntity.ok(new Response<>(200, "nộp thành công", correct.getSecond() + "/" + quizTest.getCountQuestion()));
    }

    @Override
    public ResponseEntity<Response<Object>> getSubmit(String classId, Long quizTestId, SearchRequest searchRequest) {
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit(), sort);
        Page<QuizSubmit> quizSubmitPage = quizSubmitRepository.findByStudent_NameContainingIgnoreCaseAndQuizTest_Id(searchRequest.getKeyword(), quizTestId, pageable);
        List<QuizSubmitDTO> quizSubmits = quizSubmitPage.getContent().stream().map(
                quizSubmit -> new QuizSubmitDTO(
                        quizSubmit.getId(),
                        quizSubmit.getStudent().getName(),
                        quizSubmit.getScore(),
                        quizSubmit.getSubmissionTime()
                )
        ).toList();
        Map<String, Object> response = PageToDTOUltil.pageToDTO(quizSubmitPage, quizSubmits);
        return ResponseEntity.ok(new Response<>(200, "ok", response));
    }
}
