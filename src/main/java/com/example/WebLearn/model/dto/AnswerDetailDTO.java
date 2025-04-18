package com.example.WebLearn.model.dto;

import com.example.WebLearn.enumm.StatusAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailDTO {
    private Long questionId;
    private String title;
    private String answer;
    private StatusAnswer statusAnswer;
    private String result;
}
