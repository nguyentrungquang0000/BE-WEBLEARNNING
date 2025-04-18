package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassByUserDTO {
    private Long id;
    private String className;
    private String classId;
    private int countAssignment;
    private int countStudent;
    private int countLecture;
    private int countTest;
}
