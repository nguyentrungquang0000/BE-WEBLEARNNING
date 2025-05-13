package com.example.WebLearn.entity;

import com.example.WebLearn.enumm.RoleEnum;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.lang.annotation.Documented;
import java.util.Date;

@Document(collection = "Chat")
@CompoundIndex(name = "class_date_id_idx", def = "{'classId': 1, 'date': -1, '_id': -1}", background = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDocument {
    @MongoId
    private String id;
    private String content;
    private String role;
    private Long senderId;
    @Indexed(name = "classId_idx")
    private String classId;

    @CreatedDate
    @Indexed(name = "date_idx")
    private Date date;

    //Tính năng ghim
    private boolean isPinned;

    //Tính năng gỡ tin nhắn
    private boolean isDeleted;
}
