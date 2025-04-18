package com.example.WebLearn.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {
    private String title;
    private String description;
    private String fileUrl;
    private Date dueDate;
}
