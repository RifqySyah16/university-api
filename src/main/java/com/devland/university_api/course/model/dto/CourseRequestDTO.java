package com.devland.university_api.course.model.dto;

import com.devland.university_api.course.model.Course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {
    @Positive(message = "ID must be positive number or greter than zero")
    @NotNull(message = "ID is required")
    private int id;
    @NotBlank(message = "Course name is required")
    private String courseName;
    @NotBlank(message = "Course description is required")
    private String description;

    public CourseRequestDTO(@NotBlank(message = "Course name is required") String courseName,
            @NotBlank(message = "Course description is required") String description) {
        this.courseName = courseName;
        this.description = description;
    }

    public Course convertToEntity() {
        return Course.builder()
                .id(this.id)
                .courseName(this.courseName)
                .description(this.description)
                .build();
    }
}
