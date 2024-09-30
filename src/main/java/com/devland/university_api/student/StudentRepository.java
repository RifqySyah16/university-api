package com.devland.university_api.student;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devland.university_api.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByName(String name);

    Page<Student> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    List<Student> findAllByIdIn(List<Integer> studentIds);

}
