package com.example.WebLearn.entity;

import com.example.WebLearn.enumm.StatusAnswer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="answerdetail")
@Data
public class AnswerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;
    @Enumerated(EnumType.STRING)
    private StatusAnswer statusAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "quizsubmit_id")
    private QuizSubmit quizSubmit;

}
