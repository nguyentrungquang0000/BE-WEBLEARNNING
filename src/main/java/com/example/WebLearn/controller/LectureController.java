package com.example.WebLearn.controller;

import com.example.WebLearn.entity.Lecture;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class LectureController {
    @Autowired
    private LectureService lectureService;
    @PostMapping("/class/{classId}/lecture")
    public ResponseEntity<Response<Object>> createLecture(@PathVariable String classId,
                                                          @RequestParam("title") String title,
                                                          @RequestParam(value = "description", required = false) String description,
                                                          @RequestPart("multipartFile") MultipartFile file){
        return lectureService.createLecture(classId, title, description, file);
    }

    @PutMapping("/class/{classId}/lecture/{lectureId}")
    public ResponseEntity<Response<Object>> updateLecture(@PathVariable String classId,
                                                          @PathVariable Long lectureId,
                                                          @RequestParam("title") String title,
                                                          @RequestParam(value = "description", required = false) String description,
                                                          @RequestPart(value = "multipartFile", required = false) MultipartFile file,
                                                          @RequestParam(value = "change", required = false) String change
                                                          ){
        return lectureService.updateLecture(classId, lectureId, title, description, file, change);
    }

    @DeleteMapping("/class/{classId}/d/{lectureId}")
    public ResponseEntity<Response<Object>> deleteLecture(@PathVariable String classId, @PathVariable Long lectureId){
        return lectureService.deleteLecture(classId, lectureId);
    }

    @GetMapping("/class/{classId}/lecture")
    public ResponseEntity<Response<Object>> getLecture(@PathVariable String classId){
        return lectureService.getLecture(classId);
    }
}
