package com.example.WebLearn.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="quiztest")
@Data
public class QuizTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int countQuestion;
    private int examTime;

    @CreationTimestamp // Thời gian đăng
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Date startTime;

    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @OneToMany(mappedBy = "quizTest")
    private List<Question> questions;

    @OneToMany(mappedBy = "quizTest")
    private List<QuizSubmit> quizSubmits;
}
