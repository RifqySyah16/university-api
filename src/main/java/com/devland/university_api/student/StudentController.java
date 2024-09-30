package com.devland.university_api.student;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devland.university_api.student.model.Student;
import com.devland.university_api.student.model.dto.StudentRequestDTO;
import com.devland.university_api.student.model.dto.StudentResponseDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAll(
            @RequestParam("name") Optional<String> optionalName,
            @RequestParam(value = "sort", defaultValue = "ASC") String sortString,
            @RequestParam(value = "order_by", defaultValue = "id") String orderBy,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortString), "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Student> pageStudents = this.studentService.getAll(optionalName, pageable);
        Page<StudentResponseDTO> studentResponseDTOs = pageStudents.map(Student::convertToResponse);

        return ResponseEntity.ok(studentResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getOne(@PathVariable("id") int id) {
        Student existingStudent = this.studentService.getOne(id);
        StudentResponseDTO studentResponseDTO = existingStudent.convertToResponse();

        return ResponseEntity.ok(studentResponseDTO);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(@RequestBody @Valid StudentRequestDTO studentRequestDTO) {
        Student newStudent = studentRequestDTO.convertToEntity();

        Student savedStudent = this.studentService.create(newStudent);
        StudentResponseDTO studentResponseDTO = savedStudent.convertToResponse();

        return ResponseEntity.status(HttpStatus.CREATED).body(studentResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> update(@PathVariable("id") int id,
            @RequestBody StudentRequestDTO studentRequestDTO) {
        Student updatedStudent = studentRequestDTO.convertToEntity();
        updatedStudent.setId(id);

        Student savedStudent = this.studentService.update(updatedStudent);
        StudentResponseDTO studentResponseDTO = savedStudent.convertToResponse();

        return ResponseEntity.ok(studentResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.studentService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/downloadable-students")
    public void generateEcxelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=students.xlsx";

        response.setHeader(headerKey, headerValue);

        this.studentService.generateExcel(response);
    }

    @PostMapping("/uploadable-students")
    public ResponseEntity<List<StudentResponseDTO>> uploadExcel(@RequestParam("file") MultipartFile file)
            throws IOException {
        List<StudentResponseDTO> savedStudents = this.studentService.saveStudentsFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudents);
    }

}
