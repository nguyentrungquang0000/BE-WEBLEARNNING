package com.example.WebLearn.model.request;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizTestRequest {
    private String name;
    private String description;
    private int countQuestion;
    private int examTime;
    private Date startTime;
    private Date endTime;
    List<QuestionRequest> questions;
}
