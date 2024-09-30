package com.devland.university_api.classroom.model.dto;

import java.sql.Timestamp;
import java.util.List;

import com.devland.university_api.course.model.dto.CourseResponseDTO;
import com.devland.university_api.student.model.dto.StudentResponseDTO;
import com.devland.university_api.teacher.model.dto.TeacherResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomResponseDTO {
    private int id;
    private String className;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private TeacherResponseDTO teacherResponseDTO;
    private List<StudentResponseDTO> studentResponseDTO;
    private CourseResponseDTO courseResponseDTO;
}
