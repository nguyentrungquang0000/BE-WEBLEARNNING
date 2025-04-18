package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.PageToDTOUltil;
import com.example.WebLearn.entity.Assignment;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.model.dto.AssignmentDTO;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.AssignmentRepository;
import com.example.WebLearn.repository.AssignmentSubmitRepository;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.service.AssignmentService;
import com.example.WebLearn.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private AssignmentSubmitRepository assignmentSubmitRepository;

    @Override
    public ResponseEntity<Response<Object>> createAssignment(String classId, String title, String description, Date dueDate, MultipartFile file) {
        Assignment assignment = new Assignment();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        if(!classroom.getTeacher().getEmail().equals(email)) {
            return ResponseEntity.status(500).body(new Response<>(500,"Lỗi quyền", null));
        }
        try {
            if(!file.isEmpty()){
                assignment.setFileUrl(driverService.uploadFileToDrive(file));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(500, e.getMessage(), null));
        }
        assignment.setClassroom(classroom);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignmentRepository.save(assignment);
        return ResponseEntity.ok(new Response<>(200,"Tạo thành công", null));
    }

    @Override
    @Transactional
    public ResponseEntity<Response<Object>> deleteAssignment(String classId, Long assId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        if(!classroom.getTeacher().getEmail().equals(email)) {
            return ResponseEntity.status(500).body(new Response<>(500,"Lỗi quyền", null));
        }
        assignmentSubmitRepository.deleteByAssignment_Id(assId);
        Assignment assignment = assignmentRepository.findById(assId).orElseThrow();
        try {
            // Gọi phương thức xóa file từ Google Drive
            if (!assignment.getFileUrl().equals("")){
                driverService.deleteFileToDrive(assignment.getFileUrl());
            }
            // Xóa assignment trong cơ sở dữ liệu
            assignmentRepository.deleteById(assId);
            return ResponseEntity.ok(new Response<>(200, "Xóa bài tập thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(500, "Lỗi khi xóa bài tập: " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<Response<Object>> getAssignments(String classId, SearchRequest searchRequest) {
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.page, searchRequest.limit, sort);
        Page<Assignment> page = assignmentRepository.findByTitleContainingIgnoreCaseAndClassroom_Id(searchRequest.keyword, classId ,pageable);

        // Chuyển đổi các Assignment thành AssignmentDTO
        List<AssignmentDTO> assignmentDTOs = page.getContent().stream()
                .map(assignment -> new AssignmentDTO(
                        assignment.getId(),
                        assignment.getTitle(),
                        assignment.getDescription(),
                        assignment.getFileUrl(),
                        assignment.getDueDate(),
                        assignment.getCreatedAt()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = PageToDTOUltil.pageToDTO(page, assignmentDTOs);
        return ResponseEntity.ok(new Response<>(200, "success", response));
    }
}
