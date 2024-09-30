package com.devland.university_api.course.model;

import java.util.List;

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.course.model.dto.CourseResponseDTO;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String courseName;
    private String description;
    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Classroom> classrooms;

    public CourseResponseDTO convertToResponse() {

        return CourseResponseDTO.builder()
                .id(this.id)
                .courseName(this.courseName)
                .description(this.description)
                .build();
    }
}
