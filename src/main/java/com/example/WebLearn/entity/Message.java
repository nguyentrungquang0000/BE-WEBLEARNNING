package com.example.WebLearn.entity;

import com.example.WebLearn.enumm.StatusMessage;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime = new Date();  // Thời gian gửi tin nhắn
    @Enumerated(EnumType.STRING)
    private StatusMessage statusMessage;

    @ManyToOne
    @JoinColumn(name = "classmember_id")
    private ClassMember classMember;

}
