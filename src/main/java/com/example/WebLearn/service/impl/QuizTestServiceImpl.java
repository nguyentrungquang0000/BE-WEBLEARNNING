package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.PageToDTOUltil;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.QuizTest;
import com.example.WebLearn.model.dto.QuizTestDTO;
import com.example.WebLearn.model.request.QuizTestRequest;
import com.example.WebLearn.model.request.QuizUpdateRequest;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.QuizTestRepository;
import com.example.WebLearn.service.QuestionService;
import com.example.WebLearn.service.QuizTestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuizTestServiceImpl implements QuizTestService {
    @Autowired
    private QuizTestRepository quizTestRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private QuestionService questionService;

    @Override
    public ResponseEntity<Response<Object>> createQuizzTest(String classId, QuizTestRequest quizTestRequest) {
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        QuizTest quizTest = modelMapper.map(quizTestRequest, QuizTest.class);
        quizTest.setClassroom(classroom);
        questionService.saveQuestion(quizTestRepository.save(quizTest).getId(),quizTestRequest.getQuestions());
        return ResponseEntity.ok(new Response<>(200, "Tạo thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getQuizTest(String classId, SearchRequest searchRequest) {
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.page, searchRequest.limit, sort);
        Page<QuizTest> quizTests = quizTestRepository.findByNameContainingIgnoreCaseAndClassroom_Id(searchRequest.keyword, classId, pageable);
        List<QuizTestDTO> quizTestDTOs = quizTests.getContent().stream().map(
                quizTest -> modelMapper.map(quizTest, QuizTestDTO.class)
        ).toList();
        Map<String, Object> response = PageToDTOUltil.pageToDTO(quizTests, quizTestDTOs);
        return ResponseEntity.ok(new Response<>(200, "ok", response));
    }

    @Override
    public ResponseEntity<Response<Object>> getQuizDetail(String classId, Long quizId) {
        QuizTest quizTest = quizTestRepository.findById(quizId).orElseThrow();
        QuizTestDTO quizTestDTO = modelMapper.map(quizTest, QuizTestDTO.class);
        return ResponseEntity.ok(new Response<>(200, "ok", quizTestDTO));
    }

    @Override
    public ResponseEntity<Response<Object>> updateQuizInfo(Long quizId, QuizUpdateRequest quizUpdateRequest) {
        QuizTest quizTest = quizTestRepository.findById(quizId).orElseThrow();
        modelMapper.map(quizUpdateRequest, quizTest);
        quizTestRepository.save(quizTest);
        return ResponseEntity.ok(new Response<>(200, "Sửa thành công !", null));
    }
}
