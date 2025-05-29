package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDashboardDTO {
    private int countLecture;
    private int countAssignment;
    private Long countStudent;
    private int countTest;
    private String className;
    private String teacherName;
    private String classId;
}
