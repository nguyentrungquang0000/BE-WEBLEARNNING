package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestDTO {
    private Long questionId;
    private String title;
    private String answer;
}
