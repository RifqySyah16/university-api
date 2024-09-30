package com.devland.university_api.teacher;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devland.university_api.teacher.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByName(String name);

    Page<Teacher> findAllByNameContainsIgnoreCase(String name, Pageable pageable);
}
