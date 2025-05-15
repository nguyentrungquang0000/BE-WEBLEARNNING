package com.example.WebLearn.service.QuizSubmitRedis;

import com.example.WebLearn.entity.Question;
import com.example.WebLearn.entity.QuizSubmit;
import com.example.WebLearn.entity.QuizTest;
import com.example.WebLearn.model.dto.QuestionTestDTO;
import com.example.WebLearn.model.dto.QuizTesttingDTO;
import com.example.WebLearn.model.request.AnswerDetailRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.QuizSubmitRepository;
import com.example.WebLearn.repository.QuizTestRepository;
import com.example.WebLearn.service.QuizSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizSubmitRedisServiceImpl implements QuizSubmitRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final QuizTestRepository quizTestRepository;
    private final QuizSubmitRepository quizSubmitRepository;
    private final QuizSubmitService quizSubmitService;

    @Override
    public ResponseEntity<Void> saveQuizStart(String classId, Long quizId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        QuizSubmit quizSubmit = quizSubmitRepository.findByQuizTest_IdAndStudent_Email(quizId, email).orElse(null);
        if (quizSubmit == null) {
            return (ResponseEntity<Void>) ResponseEntity.status(500);
        }
        QuizTest quizTest = quizTestRepository.findById(quizId).orElseThrow();
        List<Question> questions = quizTest.getQuestions();
        Map<String, String> answers = new HashMap<>();
        for (Question question : questions) {
            answers.put(question.getId().toString(), "");
        }
        String key = getKey(quizId);
        hashOperations.put(key, QuizSubmit.START_TIME, new Date(System.currentTimeMillis()));
        hashOperations.put(key, "answers", answers);
        String expirationKey = getExpireKey(quizId);
        this.redisTemplate.opsForValue().set(expirationKey, "", Duration.ofMinutes(quizTest.getExamTime()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateAnswer(String classId, Long quizId, AnswerDetailRequest answerDetailRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = "quiz:info:" + email + ":" + quizId;
        Map<String, String> answers = (Map<String, String>) this.hashOperations.get(key, "answers");
        String answerDetail = answers.getOrDefault(answerDetailRequest.getQuestionId().toString(), null);
        if (answerDetail == null) {
            return ResponseEntity.badRequest().build();
        }
        answers.put(String.valueOf(answerDetailRequest.getQuestionId()), answerDetailRequest.getAnswer());
        hashOperations.put(key, "answers", answers);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Response<Object>> getQuizStatus(Long quizId) {
        // Kiểm tra làm chưa
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        QuizSubmit quizSubmit = quizSubmitRepository.findByQuizTest_IdAndStudent_Email(quizId, email).orElse(null);
        if (quizSubmit != null) {
            return ResponseEntity.ok(new Response<>(200, quizSubmit.getId().toString(), "SUBMITTED"));
        }
        // check đang làm
        String key = "quiz:info:" + email + ":" + quizId;
        if (hashOperations.hasKey(key, QuizSubmit.START_TIME)) {
            return ResponseEntity.ok(new Response<>(200, "Đang làm", "SUBMITTING"));
        }

        return ResponseEntity.ok(new Response<>(200, "Chưa làm", "NO_SUBMIT"));
    }

    @Override
    public ResponseEntity<Response<Object>> submitQuiz(Long quizId, String email) {
        String key = "quiz:info:" + email + ":" + quizId;
        String expirationKey = "quiz:expiration:" + email + ":" + quizId;
        List<AnswerDetailRequest> answerDetailRequests = new ArrayList<>();
        Map<String, String> answer = (Map<String, String>) hashOperations.get(key, "answers");
        for (Map.Entry<String, String> entry : answer.entrySet()) {
            AnswerDetailRequest answerDetailRequest = new AnswerDetailRequest();
            answerDetailRequest.setQuestionId(Long.valueOf(entry.getKey()));
            answerDetailRequest.setAnswer(entry.getValue());
            answerDetailRequests.add(answerDetailRequest);
        }
        redisTemplate.delete(key);
        if (redisTemplate.hasKey(expirationKey)) redisTemplate.delete(expirationKey);
        return quizSubmitService.saveSubmit(answerDetailRequests, quizId, email);
    }
// đang làm
    @Override
    public ResponseEntity<Response<Object>> getQuizzing(Long quizId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = "quiz:info:" + email + ":" + quizId;
        QuizTest quizTest = quizTestRepository.findById(quizId).orElseThrow();
        List<Question> questions = quizTest.getQuestions();
        Map<String, String> answers = new HashMap<>();
        Date startTime= new Date();
        if(hashOperations.hasKey(key, QuizSubmit.START_TIME)){
            answers = (Map<String, String>) hashOperations.get(key, "answers");
            startTime = (Date) hashOperations.get(key, QuizSubmit.START_TIME);
        };
        List<QuestionTestDTO> questionTestDTOs = new ArrayList<>();
        for (Question question : questions) {
            QuestionTestDTO questionTestDTO = new QuestionTestDTO();
            questionTestDTO.setQuestionId(question.getId());
            questionTestDTO.setTitle(question.getTitle());
            questionTestDTO.setAnswer(answers.getOrDefault(question.getId().toString(), null));
            questionTestDTOs.add(questionTestDTO);
        }
        QuizTesttingDTO quizTesttingDTO = new QuizTesttingDTO();
        quizTesttingDTO.setStartTime(startTime);
        quizTesttingDTO.setQuestions(questionTestDTOs);
        quizTesttingDTO.setExamTime(quizTest.getExamTime());
        return ResponseEntity.ok(new Response<>(200, "ok", quizTesttingDTO));
    }

    private String getKey(Long quizId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "quiz:info:" + email + ":" + quizId;
    }

    private String getExpireKey(Long quizId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "quiz:expiration:" + email + ":" + quizId;
    }
}
