package com.devland.university_api.course;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devland.university_api.course.model.Course;
import com.devland.university_api.course.model.dto.CourseRequestDTO;
import com.devland.university_api.course.model.dto.CourseResponseDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> getAll(
            @RequestParam("coursename") Optional<String> optionalCourseName,
            @RequestParam(value = "sort", defaultValue = "ASC") String sortString,
            @RequestParam(value = "order_by", defaultValue = "id") String orderBy,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortString), "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Course> pageCourses = this.courseService.getAll(optionalCourseName, pageable);
        Page<CourseResponseDTO> courseResponseDTOs = pageCourses.map(Course::convertToResponse);

        return ResponseEntity.ok(courseResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getOne(@PathVariable("id") int id) {
        Course existingCourse = this.courseService.getOne(id);
        CourseResponseDTO courseResponseDTO = existingCourse.convertToResponse();

        return ResponseEntity.ok(courseResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {
        Course newCourse = courseRequestDTO.convertToEntity();

        Course savedCourse = this.courseService.create(newCourse);
        CourseResponseDTO courseResponseDTO = savedCourse.convertToResponse();

        return ResponseEntity.status(HttpStatus.CREATED).body(courseResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable("id") int id,
            @RequestBody CourseRequestDTO courseRequestDTO) {
        Course updatedCourse = courseRequestDTO.convertToEntity();
        updatedCourse.setId(id);
        Course savedCourse = this.courseService.update(updatedCourse);
        CourseResponseDTO courseResponseDTO = savedCourse.convertToResponse();

        return ResponseEntity.ok(courseResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.courseService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/downloadable-courses")
    public void generateEcxelReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=students.xlsx";

        response.setHeader(headerKey, headerValue);

        courseService.generateExcel(response);
    }

}
