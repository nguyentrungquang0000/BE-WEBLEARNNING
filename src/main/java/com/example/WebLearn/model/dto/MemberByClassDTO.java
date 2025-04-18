package com.example.WebLearn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberByClassDTO {
    private Long id;
    private String studentName;
    private String sex;
    private String phone;
}
