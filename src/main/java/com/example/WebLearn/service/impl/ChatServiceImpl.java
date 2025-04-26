package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.ChatDocument;
import com.example.WebLearn.entity.Classroom;
import com.example.WebLearn.entity.Student;
import com.example.WebLearn.enumm.RoleEnum;
import com.example.WebLearn.model.dto.MessageDTO;
import com.example.WebLearn.model.request.MessageRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ChatRepository;
import com.example.WebLearn.repository.ClassroomRepository;
import com.example.WebLearn.repository.StudentRepository;
import com.example.WebLearn.repository.TeacherRepository;
import com.example.WebLearn.service.ChatService;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired private ChatRepository chatRepository;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;

    @Override
    public MessageDTO saveMessage(String classId, MessageRequest messageRequest) {

        ChatDocument chatDocument = new ChatDocument();
        MessageDTO messageDTO = new MessageDTO();
        chatDocument.setClassId(classId);
        chatDocument.setContent(messageRequest.getContent());
        if (messageRequest.getRole().equals("ADMIN")) {
            chatDocument.setRole(RoleEnum.TEACHER);
            Classroom classroom = classroomRepository.findById(classId).orElseThrow(() -> new RuntimeException("Classroom not found"));
            messageDTO.setSender(classroom.getTeacher().getName());
            chatDocument.setSenderId(classroom.getTeacher().getId());
        }else if(messageRequest.getRole().equals("USER")){
            chatDocument.setRole(RoleEnum.STUDENT);
            Student student = studentRepository.findById(messageRequest.getUserId()).orElseThrow(() -> new RuntimeException("Sender not found"));
            messageDTO.setSender(student.getName());
            chatDocument.setSenderId(student.getId());
        }
        chatDocument.setDeleted(false);
        chatDocument.setPinned(false);
        ChatDocument save = chatRepository.save(chatDocument);
        messageDTO.setId(save.getId());
        messageDTO.setContent(messageRequest.getContent());
        messageDTO.setPinned(false);
        messageDTO.setDeleted(false);
        messageDTO.setSenderId(messageRequest.getUserId());
        messageDTO.setCreatedAt(save.getDate());
        return messageDTO;
    }

    @Override
    public ResponseEntity<Response<Object>> getMessageOld(String classId, String lastMessageId, int limit) {
        List<ChatDocument> chatDocuments ;
        Date lastTime;
        String lastId ;
        if(lastMessageId == null || lastMessageId.isEmpty()){
            Pageable pageable1 = PageRequest.of(0, limit, Sort.by("date").descending());
            chatDocuments = chatRepository.findByClassId(classId, pageable1);
        }else{
            Pageable pageable = PageRequest.of(0, limit);
            ChatDocument chatDocument = chatRepository.findById(lastMessageId).orElse(null);
            if (chatDocument == null) {
                return ResponseEntity.status(500).body(new Response<>(500, "Message not found", null));
            }
            lastTime = chatDocument.getDate();
            lastId = chatDocument.getId();

            pageable = PageRequest.of(0, limit);
            chatDocuments = chatRepository.getMessageOld(classId, lastTime, lastId, pageable);
        }
        Collections.reverse(chatDocuments);
        List<MessageDTO> messageDTOs = chatDocuments.stream().map(
                message -> new MessageDTO(
                        message.getId(),
                        message.getContent(),
                        getNameSender(message.getRole().toString(), message.getSenderId()),
                        message.isDeleted(),
                        message.isPinned(),
                        message.getDate(),
                        message.getSenderId()
                )
        ).toList();
        return ResponseEntity.ok(new Response<>(200, "OK", messageDTOs));
    }
    public String getNameSender(String role, Long senderId) {
        if(role.equals("TEACHER")){
            return teacherRepository.findById(senderId).orElse(null).getName();
        }else if(role.equals("STUDENT")){
            return studentRepository.findById(senderId).orElse(null).getName();
        }
        throw new RuntimeException();
    }
}
