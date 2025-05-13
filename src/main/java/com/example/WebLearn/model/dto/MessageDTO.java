package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String id;
    private String content;
    private String sender;
    private boolean isDeleted;
    private boolean isPinned;
    private Date createdAt;
    private Long senderId;
    private String role;
}
