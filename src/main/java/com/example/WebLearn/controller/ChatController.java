package com.example.WebLearn.controller;

import com.example.WebLearn.entity.ChatDocument;
import com.example.WebLearn.model.dto.MessageDTO;
import com.example.WebLearn.model.request.MessageRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.ChatRepository;
import com.example.WebLearn.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatService chatService;


    @MessageMapping("/class/{classId}/send")
    public void sendMessage(@DestinationVariable String classId,
                            @Payload MessageRequest messageRequest) {

        try {
            MessageDTO messageDTO = chatService.saveMessage(classId, messageRequest);
            messagingTemplate.convertAndSend("/topic/class/" + classId, messageDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/class/{classId}/messold")
    public ResponseEntity<Response<Object>> getMessageOld(@PathVariable String classId,
                                                       @RequestParam(defaultValue = "") String lastMessageId,
                                                       @RequestParam(defaultValue = "10") int limit) {
        return chatService.getMessageOld(classId, lastMessageId, limit);
    }

}
