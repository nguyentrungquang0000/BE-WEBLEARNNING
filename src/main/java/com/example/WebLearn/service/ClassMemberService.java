package com.example.WebLearn.service;

import com.example.WebLearn.entity.ClassMember;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClassMemberService {
    ResponseEntity<Response<Object>> joinClassMember(String classId);
    ResponseEntity<Response<Object>> removeClassMember(Long classMemberId, String classId);
    ResponseEntity<Response<Object>> updateClassMember(Long classMemberId, String classId);
    ResponseEntity<Response<Object>> getClassByUser(SearchRequest searchRequest);
    ResponseEntity<Response<Object>> getMemberByClass(String classId, SearchRequest searchRequest);
    ResponseEntity<Response<Object>> getPendingByClass(String classId);
    ResponseEntity<Response<Object>> leaveClassMember(String classId);
}
