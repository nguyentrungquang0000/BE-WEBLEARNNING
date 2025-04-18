package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitDTO {
    private Long id;
    private String studentName;
    private float score;
    private Date submissionTime;
}
