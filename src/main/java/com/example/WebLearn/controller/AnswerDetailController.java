package com.example.WebLearn.controller;

import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.AnswerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerDetailController {
    @Autowired
    private AnswerDetailService answerDetailService;
    @GetMapping("class/{classId}/test/{testId}/submit/{submitId}")
    public ResponseEntity<Response<Object>> getAnswerDetail(@PathVariable("classId") String classId,
                                                            @PathVariable Long testId,
                                                            @PathVariable Long submitId) {
        return answerDetailService.getAnswerDetail(classId, testId, submitId);
    }
}
