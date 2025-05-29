package com.example.WebLearn.controller;

import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PreAuthorize("@classPermission.hasAccess(#classId)")
    @GetMapping("/class/{classId}/test/{quizTestId}/question")
    public ResponseEntity<Response<Object>> getQuestion(@PathVariable("classId") String classId,
                                                        @PathVariable("quizTestId") Long quizTestId) {
        return questionService.getQuestion(classId, quizTestId);
    }
}
