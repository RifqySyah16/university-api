package com.devland.university_api.teacher.model;

import java.util.List;

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.teacher.model.dto.TeacherResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String address;
    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Classroom> classrooms;

    public TeacherResponseDTO convertToResponse() {
        return TeacherResponseDTO.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .build();
    }
}
