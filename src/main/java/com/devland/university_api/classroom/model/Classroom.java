package com.devland.university_api.classroom.model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.devland.university_api.classroom.model.dto.ClassroomResponseDTO;
import com.devland.university_api.course.model.Course;
import com.devland.university_api.course.model.dto.CourseResponseDTO;
import com.devland.university_api.student.model.Student;
import com.devland.university_api.student.model.dto.StudentResponseDTO;
import com.devland.university_api.teacher.model.Teacher;
import com.devland.university_api.teacher.model.dto.TeacherResponseDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String className;
    private String description;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public ClassroomResponseDTO convertToResponse() {
        TeacherResponseDTO teacherResponseDTO = this.teacher.convertToResponse();
        List<StudentResponseDTO> studentResponseDTOs = this.students.stream().map(Student::convertToResponse).toList();
        CourseResponseDTO courseResponseDTO = this.course.convertToResponse();

        return ClassroomResponseDTO.builder()
                .id(this.id)
                .className(this.className)
                .description(this.description)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .teacherResponseDTO(teacherResponseDTO)
                .studentResponseDTO(studentResponseDTOs)
                .courseResponseDTO(courseResponseDTO)
                .build();
    }
}
