package com.example.WebLearn.entity;

import com.example.WebLearn.enumm.StatusClassMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="classmember")
@Data
public class ClassMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusClassMember statusClassMember;
    @ManyToOne
    @JoinColumn(name="classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @OneToMany(mappedBy = "classMember")
    private List<Message> messages;
}
