package com.example.WebLearn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="question")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String result;

    @ManyToOne
    @JoinColumn(name = "quiztest_id")
    @JsonIgnore
    private QuizTest quizTest;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<AnswerDetail> answerDetails;
}
