package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
    private Long id;
    private String title;
    private String description;
    private String lectureUrl;
    private Date createdAt;
}
