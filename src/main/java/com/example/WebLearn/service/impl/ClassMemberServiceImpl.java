package com.example.WebLearn.service.impl;

import com.example.WebLearn.Utils.AuthorizationCheckUtil;
import com.example.WebLearn.Utils.PageToDTOUltil;
import com.example.WebLearn.entity.ClassMember;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.Student;
import com.example.WebLearn.enumm.StatusClassMember;
import com.example.WebLearn.model.dto.ClassByUserDTO;
import com.example.WebLearn.model.dto.MemberByClassDTO;
import com.example.WebLearn.model.dto.PendingByClassDTO;
import com.example.WebLearn.model.request.SearchRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ClassMemberRepository;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.StudentRepository;
import com.example.WebLearn.service.ClassMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClassMemberServiceImpl implements ClassMemberService {
    @Autowired
    private ClassMemberRepository classMemberRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private AuthorizationCheckUtil authorizationCheckUtil;
    @Override
    public ResponseEntity<Response<Object>> joinClassMember(String classId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ClassMember member = classMemberRepository.findByClassroom_IdAndStudent_Email(classId, email);
        if (member != null) {
            return ResponseEntity.ok(new Response<>(201, "Học sinh đã gửi yêu cầu", null));
        }
        Student student = studentRepository.findByEmail(email).orElseThrow();
        Classroom classroom = classroomRepository.findById(classId).orElseThrow();
        ClassMember classMember = new ClassMember();
        classMember.setStudent(student);
        classMember.setClassroom(classroom);
        classMember.setStatusClassMember(StatusClassMember.PENDING);
        classMemberRepository.save(classMember);
        return ResponseEntity.ok(new Response<>(200, "Đề nghị tham gia thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> removeClassMember(Long classMemberId, String classId) {

        if(!authorizationCheckUtil.checkUserAccessToClassroom(classId)) {
            return ResponseEntity.ok(new Response<>(401, "Lỗi phân quyền", null));
        }
        ClassMember classMember = classMemberRepository.findById(classMemberId).orElseThrow();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!email.equals(classMember.getClassroom().getTeacher().getEmail())){
            return ResponseEntity.status(403).body(new Response<>(403,"Lỗi quyền", null));
        }
        classMemberRepository.deleteById(classMemberId);
        return ResponseEntity.ok(new Response<>(200, "Xoá thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> updateClassMember(Long classMemberId, String classId) {
        ClassMember classMember = classMemberRepository.findById(classMemberId).orElseThrow();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!email.equals(classMember.getClassroom().getTeacher().getEmail())){
            return ResponseEntity.status(403).body(new Response<>(403,"Lỗi quyền", null));
        }
        classMember.setStatusClassMember(StatusClassMember.APPROVED);
        classMemberRepository.save(classMember);
        return ResponseEntity.ok(new Response<>(200, "Duyệt thành viên thành công", null));
    }

    @Override
    public ResponseEntity<Response<Object>> getClassByUser(SearchRequest searchRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit(), sort);
        Map<String, Object> response ;
        if(role.contains("ROLE_USER")){
            Student student = studentRepository.findByEmail(email).orElseThrow();
            Page<ClassMember> classMemberPage = classMemberRepository.findByClassroom_NameContainingIgnoreCaseAndStudent_IdAndStatusClassMember(searchRequest.getKeyword(), student.getId(), StatusClassMember.APPROVED, pageable);
            List<ClassByUserDTO> classByUserDTOs = classMemberPage.stream().map(
                    classMember -> new ClassByUserDTO(
                            classMember.getId(),
                            classMember.getClassroom().getName(),
                            classMember.getClassroom().getId(),
                            classMember.getClassroom().getAssignments().size(),
                            classMember.getClassroom().getClassMembers().size(),
                            classMember.getClassroom().getLectures().size(),
                            classMember.getClassroom().getQuizTests().size()
                    )
            ).toList();
            response = PageToDTOUltil.pageToDTO(classMemberPage, classByUserDTOs);
        }else if(role.contains("ROLE_ADMIN")){
            Page<Classroom> classroomPage = classroomRepository.findByNameContainingIgnoreCaseAndTeacher_Email(searchRequest.getKeyword(), email, pageable);
            List<ClassByUserDTO> classByUserDTOs = classroomPage.getContent().stream().map(
                    classroom -> new ClassByUserDTO(
                            0L,
                            classroom.getName(),
                            classroom.getId(),
                            classroom.getAssignments().size(),
                            classroom.getClassMembers().size(),
                            classroom.getLectures().size(),
                            classroom.getQuizTests().size()
                    )
            ).toList();
            response = PageToDTOUltil.pageToDTO(classroomPage, classByUserDTOs);
        }else{
            return ResponseEntity.status(500).body(new Response<>(500, "error", null));
        }
        return ResponseEntity.ok(new Response<>(200, "ok", response));
    }

    @Override
    public ResponseEntity<Response<Object>> getMemberByClass(String classId, SearchRequest searchRequest) {
        Sort sort = searchRequest.direction.equals("asc") ? Sort.by(searchRequest.sortBy).ascending() : Sort.by(searchRequest.sortBy).descending();
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit(), sort);
        Page<ClassMember> classMemberPage = classMemberRepository
                .findByStudent_NameContainingIgnoreCaseAndStatusClassMemberAndClassroom_Id(searchRequest.getKeyword(), StatusClassMember.APPROVED, classId, pageable);
        List<MemberByClassDTO> memberByClassDTOS = classMemberPage.getContent().stream().map(
                classMember -> new MemberByClassDTO(
                        classMember.getId(),
                        classMember.getStudent().getName(),
                        classMember.getStudent().getSex(),
                        classMember.getStudent().getPhone()
                )
        ).toList();
        Map<String, Object> response = PageToDTOUltil.pageToDTO(classMemberPage, memberByClassDTOS);
        return ResponseEntity.ok(new Response<>(200, "ok", response));
    }

    @Override
    public ResponseEntity<Response<Object>> getPendingByClass(String classId) {
        List<ClassMember> classMembers = classMemberRepository.findByClassroom_IdAndStatusClassMember(classId, StatusClassMember.PENDING);
        List<PendingByClassDTO> pendingByClassDTOs = classMembers.stream().map(
                classMember -> new PendingByClassDTO(
                        classMember.getId(),
                        classMember.getStudent().getName()
                )
        ).toList();
        return ResponseEntity.ok(new Response<>(200, "ok", pendingByClassDTOs));
    }
}
