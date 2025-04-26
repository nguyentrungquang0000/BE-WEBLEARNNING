package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.PageToDTOUltil;
import com.example.WebLearn.entity.Assignment;
import com.example.WebLearn.entity.AssignmentSubmit;
import com.example.WebLearn.entity.Student;
import com.example.WebLearn.enumm.StatusSubmit;
import com.example.WebLearn.model.dto.AssignmentSubmitDTO;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.AssignmentRepository;
import com.example.WebLearn.repository.AssignmentSubmitRepository;
import com.example.WebLearn.repository.StudentRepository;
import com.example.WebLearn.service.AssignmentSubmitService;
import com.example.WebLearn.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AssignmentSubmitServiceImpl implements AssignmentSubmitService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentSubmitRepository assignmentSubmitRepository;
    @Autowired
    private DriverService driverService;
    public ResponseEntity<Response<Object>> saveAssignment(String classId, Long assId, MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        Assignment assignment = assignmentRepository.findById(assId).orElseThrow();
        //Kiểm tra dueDate
        Date now = new Date();
        if(now.after(assignment.getDueDate())){
            return ResponseEntity.status(400).body(new Response<>(400, "Quá hạn", null));
        }
        AssignmentSubmit assignmentSubmit = new AssignmentSubmit();
        assignmentSubmit.setStudent(student);
        assignmentSubmit.setAssignment(assignment);
        if (!file.isEmpty()){
            try {
                assignmentSubmit.setSubmitUrl(driverService.uploadFileToDrive(file));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        assignmentSubmit.setStatusSubmit(StatusSubmit.SUBMITTED);//?
        assignmentSubmitRepository.save(assignmentSubmit);
        return ResponseEntity.ok(new Response<>(200, "Nộp thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> scoreSubmit(String classId, Long assId, Long submitId, float score) {
        AssignmentSubmit assignmentSubmit = assignmentSubmitRepository.findById(submitId).orElseThrow();
        assignmentSubmit.setScore(score);
        assignmentSubmit.setStatusSubmit(StatusSubmit.SCORED);
        assignmentSubmitRepository.save(assignmentSubmit);
        return ResponseEntity.ok(new Response<>(200, "Update Success", null));
    }

    @Override
    public ResponseEntity<Response<Object>> deleteSubmit(String classId, Long assId, Long submitId) {
        AssignmentSubmit assignmentSubmit = assignmentSubmitRepository.findById(submitId).orElseThrow();
        try{
            driverService.deleteFileToDrive(assignmentSubmit.getSubmitUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        assignmentSubmitRepository.deleteById(submitId);
        return ResponseEntity.ok(new Response<>(200, "Delete Success", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getAssignmentSubmit(String classId, Long assId, SearchRequest searchRequest) {
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.page, searchRequest.limit, sort);
        Page<AssignmentSubmit> assignmentSubmitPage = assignmentSubmitRepository.findByStudent_NameContainingIgnoreCaseAndAssignment_Id(searchRequest.getKeyword(), assId, pageable);
        List<AssignmentSubmitDTO> assignmentSubmitDTOs = assignmentSubmitPage.stream().map(
                assignmentSubmit -> new AssignmentSubmitDTO(
                        assignmentSubmit.getId(),
                        assignmentSubmit.getSubmitUrl(),
                        assignmentSubmit.getScore(),
                        assignmentSubmit.getSubmissionTime(),
                        assignmentSubmit.getStatusSubmit(),
                        assignmentSubmit.getStudent().getName()
                )
        ).toList();
        Map<String, Object> response = PageToDTOUltil.pageToDTO(assignmentSubmitPage, assignmentSubmitDTOs);
        return ResponseEntity.ok(new Response<>(200, "Success", response));
    }
}
