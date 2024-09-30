package com.devland.university_api.course;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devland.university_api.course.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCourseName(String courseName);

    Page<Course> findAllByCourseNameContainsIgnoreCase(String courseName, Pageable pageable);

}
