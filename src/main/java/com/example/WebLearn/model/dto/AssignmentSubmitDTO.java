package com.example.WebLearn.model.dto;

import com.example.WebLearn.enumm.StatusSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmitDTO {
    private Long id;
    private String submitUrl;
    private float score;
    private Date submissionTime;
    private StatusSubmit statusSubmit;
    private String studentName;
}
