package com.example.WebLearn.service;

import com.example.WebLearn.model.dto.MessageDTO;
import com.example.WebLearn.model.request.MessageRequest;
import com.example.WebLearn.model.response.Response;
import org.springframework.http.ResponseEntity;

public interface ChatService {
    MessageDTO saveMessage(String classId, MessageRequest messageRequest);
    ResponseEntity<Response<Object>> getMessageOld(String classId, String lastMessageId, int limit);
    ResponseEntity<Response<Object>> recallMessage(String classId, String messageId);
}
