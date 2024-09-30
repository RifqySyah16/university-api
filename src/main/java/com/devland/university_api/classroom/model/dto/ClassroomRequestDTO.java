package com.devland.university_api.classroom.model.dto;

import java.util.List;

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.course.model.Course;
import com.devland.university_api.course.model.dto.CourseRequestDTO;
import com.devland.university_api.student.model.Student;
import com.devland.university_api.student.model.dto.StudentRequestDTO;
import com.devland.university_api.teacher.model.Teacher;
import com.devland.university_api.teacher.model.dto.TeacherRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequestDTO {
    private int id;
    @NotBlank(message = "Class Name is required")
    private String className;
    @NotBlank(message = "Description is required")
    private String description;
    @Valid
    private TeacherRequestDTO teacherRequestDTO;
    @Valid
    private List<StudentRequestDTO> registeredStudents;
    @Valid
    private CourseRequestDTO courseRequestDTO;

    public Classroom convertToEntity() {
        Teacher teacher = this.teacherRequestDTO.convertToEntity();
        List<Student> students = this.registeredStudents.stream().map(StudentRequestDTO::convertToEntity).toList();
        Course course = this.courseRequestDTO.convertToEntity();

        return Classroom.builder()
                .id(this.id)
                .className(this.className)
                .description(this.description)
                .teacher(teacher)
                .students(students)
                .course(course)
                .build();
    }

}
