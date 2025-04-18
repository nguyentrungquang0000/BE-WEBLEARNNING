package com.example.WebLearn.controller;

import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.ClassMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClassMemberController {
    @Autowired
    private ClassMemberService classMemberService;
    @PostMapping("/class")
    public ResponseEntity<Response<Object>> joinClassMember(@RequestParam("classId") String classId){
        return classMemberService.joinClassMember(classId);
    }

    @PutMapping("/class/{classId}/member")
    public ResponseEntity<Response<Object>> updateClassMember(@RequestParam Long classMemberId,@PathVariable String classId){
        return classMemberService.updateClassMember(classMemberId, classId);
    }

    @DeleteMapping("/class/{classId}/member/{classMemberId}")
    public ResponseEntity<Response<Object>> deleteClassMember(@PathVariable String classId, @PathVariable Long classMemberId){
        return classMemberService.removeClassMember(classMemberId, classId);
    }

    @GetMapping("/class")
    public ResponseEntity<Response<Object>> getClass(@ModelAttribute SearchRequest searchRequest){
        return classMemberService.getClassByUser(searchRequest);
    }

    @GetMapping("/class/{classId}/member")
    public ResponseEntity<Response<Object>> getMemberByClass(@PathVariable String classId,
                                                             @ModelAttribute SearchRequest searchRequest){
        return classMemberService.getMemberByClass(classId, searchRequest);
    }

    @GetMapping("/class/{classId}/pending")
    public ResponseEntity<Response<Object>> getPendingByClass(@PathVariable String classId){
        return classMemberService.getPendingByClass(classId);
    }
}
