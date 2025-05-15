package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private float score;
    private String title;
    private String name;
    private Date submit_time;
    List<AnswerResultDTO> questions;
}
