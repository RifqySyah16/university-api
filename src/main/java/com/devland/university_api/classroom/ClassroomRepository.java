package com.devland.university_api.classroom;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devland.university_api.classroom.model.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    Optional<Classroom> findByClassName(String className);

    Page<Classroom> findAllByClassNameContainsIgnoreCase(String className, Pageable pageable);

}
