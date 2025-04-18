package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizTestDTO {
    private Long id;
    private String name;
    private String description;
    private int countQuestion;
    private int examTime;
    private Date createdAt;
    private Date startTime;
    private Date endTime;
}
