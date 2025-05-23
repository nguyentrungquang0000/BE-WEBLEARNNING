package com.example.WebLearn.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="quizsubmit")
@Data
public class QuizSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionTime;  // Thời gian nộp bài

    private float score;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "quizSubmit")
    private List<AnswerDetail> answerDetails;

    @ManyToOne
    @JoinColumn(name = "quiztest_id")
    private QuizTest quizTest;
}
