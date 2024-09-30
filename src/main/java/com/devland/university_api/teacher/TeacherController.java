package com.devland.university_api.teacher;

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

import com.devland.university_api.teacher.model.Teacher;
import com.devland.university_api.teacher.model.dto.TeacherRequestDTO;
import com.devland.university_api.teacher.model.dto.TeacherResponseDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<Page<TeacherResponseDTO>> findAll(
            @RequestParam("name") Optional<String> optionalName,
            @RequestParam(value = "sort", defaultValue = "ASC") String sortString,
            @RequestParam(value = "order_by", defaultValue = "id") String orderBy,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortString), "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Teacher> pageTeachers = this.teacherService.findAll(optionalName, pageable);
        Page<TeacherResponseDTO> teacherResponseDTOs = pageTeachers.map(Teacher::convertToResponse);

        return ResponseEntity.ok(teacherResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getOne(@PathVariable("id") int id) {
        Teacher existingTeacher = this.teacherService.getOne(id);
        TeacherResponseDTO teacherResponseDTO = existingTeacher.convertToResponse();

        return ResponseEntity.ok(teacherResponseDTO);
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> create(@RequestBody @Valid TeacherRequestDTO teacherRequestDTO) {
        Teacher newTeacher = teacherRequestDTO.convertToEntity();

        Teacher savedTeacher = this.teacherService.create(newTeacher);
        TeacherResponseDTO teacherResponseDTO = savedTeacher.convertToResponse();

        return ResponseEntity.status(HttpStatus.CREATED).body(teacherResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> update(@PathVariable("id") int id,
            @RequestBody TeacherRequestDTO teacherRequestDTO) {
        Teacher updatedTeacher = teacherRequestDTO.convertToEntity();
        updatedTeacher.setId(id);
        Teacher savedTeacher = this.teacherService.update(updatedTeacher);
        TeacherResponseDTO teacherResponseDTO = savedTeacher.convertToResponse();

        return ResponseEntity.ok(teacherResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.teacherService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/downloadable-teachers")
    public void generateEcxelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=students.xlsx";

        response.setHeader(headerKey, headerValue);

        teacherService.generateExcel(response);
    }

    @PostMapping("/uploadable-teachers")
    public ResponseEntity<List<TeacherResponseDTO>> uploadExcel(@RequestParam("file") MultipartFile file)
            throws IOException {
        List<TeacherResponseDTO> savedTeachers = this.teacherService.saveTeachersFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeachers);
    }

}
