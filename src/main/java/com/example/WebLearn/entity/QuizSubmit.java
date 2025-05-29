package com.example.WebLearn.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="quizsubmit")
@Data
public class QuizSubmit {
    public final static  String START_TIME = "startTime";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date startTime;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionTime;  // Thời gian nộp bài

    private float score;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "quizSubmit")
    @Cascade(value = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AnswerDetail> answerDetails;

    @ManyToOne
    @JoinColumn(name = "quiztest_id")
    private QuizTest quizTest;
}
