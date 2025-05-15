package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizTesttingDTO {
    private Date startTime;
    private int examTime;
    List<QuestionTestDTO> questions;
}
