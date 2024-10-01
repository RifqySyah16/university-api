package com.devland.university_api.course.model;

import java.util.List;

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.course.model.dto.CourseResponseDTO;

import jakarta.persistence.Column;
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

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_name", nullable = false)
    private String description;

    @OneToMany(mappedBy = "course")
    private List<Classroom> classrooms;

    public CourseResponseDTO convertToResponse() {

        return CourseResponseDTO.builder()
                .id(this.id)
                .courseName(this.courseName)
                .description(this.description)
                .build();
    }
}
