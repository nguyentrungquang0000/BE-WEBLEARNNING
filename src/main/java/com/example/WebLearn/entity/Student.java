package com.example.WebLearn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class Student extends User{
    @OneToMany(mappedBy = "student")
    private List<ClassMember> classMembers;

    @OneToMany(mappedBy = "student")
    private List<AssignmentSubmit> assignmentSubmits;
    @OneToMany(mappedBy = "student")
    private List<QuizSubmit> quizSubmits;
}
