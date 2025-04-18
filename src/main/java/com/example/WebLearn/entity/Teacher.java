package com.example.WebLearn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User{

    @OneToMany(mappedBy = "teacher")
    private List<Classroom> classrooms;
}
