package com.example.WebLearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="classroom")
@AllArgsConstructor
@Data
public class Classroom {
    @Id
    private String id;
    private String name;
    private String avatarUrl;

    public Classroom() {
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classroom")
    private List<ClassMember> classMembers;

    @OneToMany(mappedBy = "classroom")
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "classroom")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "classroom")
    private List<QuizTest> quizTests;
}
