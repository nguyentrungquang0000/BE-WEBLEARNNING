package com.example.WebLearn.entity;

import com.example.WebLearn.enumm.StatusSubmit;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name="submit")
@Data
public class AssignmentSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String submitUrl;
    private float score;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionTime;  // Thời gian nộp bài

    @Enumerated(EnumType.STRING)
    private StatusSubmit statusSubmit;

    @ManyToOne
    @JoinColumn(name = "assingment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
