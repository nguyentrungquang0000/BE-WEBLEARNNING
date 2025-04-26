package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.AuthorizationCheckUtil;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.Lecture;
import com.example.WebLearn.model.dto.LectureDTO;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.LectureRepository;
import com.example.WebLearn.service.DriverService;
import com.example.WebLearn.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private AuthorizationCheckUtil authorizationCheckUtil;
    @Override
    public ResponseEntity<Response<Object>> createLecture(String classId, String title, String description, MultipartFile file) {
        if(!authorizationCheckUtil.checkUserAccessToClassroom(classId)) {
            return ResponseEntity.status(401).body(new Response<>(401, "Không quyền truy cập", null));
        }
        if(file.isEmpty()) return ResponseEntity.ok(new Response<>(201, "không có video", null));
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        Lecture lecture = new Lecture();
        lecture.setClassroom(classroom);
        lecture.setTitle(title);
        lecture.setDescription(description);
        try {
            lecture.setLectureUrl(driverService.uploadFileToDrive(file));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        lectureRepository.save(lecture);
        return ResponseEntity.ok(new Response<>(200, "Create success", null));
    }

    @Override
    public ResponseEntity<Response<Object>> deleteLecture(String classId, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        try {
            driverService.deleteFileToDrive(lecture.getLectureUrl());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lectureRepository.deleteById(lectureId);
        return ResponseEntity.ok(new Response<>(200, "Delete success", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getLecture(String classId) {
        List<Lecture> lectures = lectureRepository.findByClassroom_Id(classId);
        List<LectureDTO> response = lectures.stream().map(
                lecture -> new LectureDTO(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getDescription(),
                        lecture.getLectureUrl(),
                        lecture.getCreatedAt()
                )).toList();
        return ResponseEntity.ok(new Response<>(200, "Get success", response));
    }

    @Override
    public ResponseEntity<Response<Object>> updateLecture(String classId, Long lectureId, String title, String description, MultipartFile file, String change) {
        if(!authorizationCheckUtil.checkUserAccessToClassroom(classId)) {
            return ResponseEntity.status(401).body(new Response<>(401, "Lỗi phân quyền", null));
        }
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            return ResponseEntity.status(400).body(new Response<>(400, "Lecture not found", null));
        }
        lecture.setTitle(title);
        lecture.setDescription(description);
        if(change.equals("true")){
            try {
                driverService.deleteFileToDrive(lecture.getLectureUrl());
                lecture.setLectureUrl(driverService.uploadFileToDrive(file));
            }catch ( Exception e){
                return ResponseEntity.status(500).body(new Response<>(500, "Upload Error", null));
            }
        }
        lectureRepository.save(lecture);
        return ResponseEntity.ok(new Response<>(200, "Update success", null));
    }
}
