package com.example.WebLearn.service;

import com.example.WebLearn.entity.Lecture;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {
    ResponseEntity<Response<Object>> createLecture(String classId, String title, String description, MultipartFile file);
    ResponseEntity<Response<Object>> deleteLecture(String classId, Long lectureId);
    ResponseEntity<Response<Object>> getLecture(String classId);
}
