package com.example.WebLearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Cascade;

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
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE})
    private List<ClassMember> classMembers;

    @OneToMany(mappedBy = "classroom")
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE})
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "classroom")
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE})
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "classroom")
    @Cascade(value = { org.hibernate.annotations.CascadeType.REMOVE})
    private List<QuizTest> quizTests;
}
