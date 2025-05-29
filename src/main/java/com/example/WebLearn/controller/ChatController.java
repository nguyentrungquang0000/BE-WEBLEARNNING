package com.example.WebLearn.controller;

import com.example.WebLearn.model.dto.MessageDTO;
import com.example.WebLearn.model.request.MessageRequest;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatService chatService;

    // Gửi tin nhắn
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

    // Phân trang
    @PreAuthorize("@classPermission.hasAccess(#classId)")
    @GetMapping("/class/{classId}/display")
    public ResponseEntity<Response<Object>> getMessageOld(@PathVariable String classId,
                                                       @RequestParam(defaultValue = "") String lastMessageId,
                                                       @RequestParam(defaultValue = "10") int limit) {
        return chatService.getMessageOld(classId, lastMessageId, limit);
    }


    // Gỡ tin nhắn
    @PreAuthorize("@classPermission.hasAccess(#classId)")
    @PutMapping("/class/{classId}/chat/d/{messageId}")
    public ResponseEntity<Response<Object>> recallMessage(@PathVariable String classId, @PathVariable String messageId) {
        return chatService.recallMessage(classId, messageId);
    }

//    @PreAuthorize("@classPermission.hasAccess(#classId)")
//    @MessageMapping("/class/{classId}/send")
//    public void recallMessage1(@DestinationVariable String classId,
//                                                           @Payload String messageId) {
//        try {
//            chatService.recallMessage(classId, messageId);
//            messagingTemplate.convertAndSend("/topic/class/" + classId, messageId);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
