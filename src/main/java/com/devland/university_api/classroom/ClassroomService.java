package com.devland.university_api.classroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.course.CourseService;
import com.devland.university_api.course.model.Course;
import com.devland.university_api.student.StudentService;
import com.devland.university_api.student.model.Student;
import com.devland.university_api.teacher.TeacherService;
import com.devland.university_api.teacher.model.Teacher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;

    public Page<Classroom> getAll(Optional<String> optionalClassName, Pageable pageable) {
        if (optionalClassName.isPresent()) {
            return this.classroomRepository.findAllByClassNameContainsIgnoreCase(optionalClassName.get(), pageable);
        }

        return this.classroomRepository.findAll(pageable);
    }

    public Classroom getOne(int id) {
        return this.classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException("Classroom Not Found"));
    }

    public Classroom create(Classroom newClassroom) {
        Optional<Classroom> existingClassroom = this.classroomRepository.findByClassName(newClassroom.getClassName());
        if (existingClassroom.isPresent()) {
            throw new ClassroomAlreadyExistException("Classroom Already Exist");
        }
        Teacher existingTeacher = this.teacherService.getOne(newClassroom.getTeacher().getId());
        newClassroom.setTeacher(existingTeacher);

        List<Student> existingStudents = this.studentService.findAllIn(newClassroom.getStudents());
        newClassroom.setStudents(existingStudents);

        Course existingCourse = this.courseService.getOne(newClassroom.getCourse().getId());
        newClassroom.setCourse(existingCourse);

        return this.classroomRepository.save(newClassroom);
    }

    public Classroom update(Classroom updatedClassroom) {
        Classroom existingClassroom = this.getOne(updatedClassroom.getId());
        updatedClassroom.setId(existingClassroom.getId());
        
        return this.classroomRepository.save(updatedClassroom);
    }

    public void delete(int id) {
        Classroom existingClassroom = this.getOne(id);
        this.classroomRepository.deleteById(existingClassroom.getId());
    }

}
