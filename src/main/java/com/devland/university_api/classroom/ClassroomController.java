package com.devland.university_api.classroom;

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

import com.devland.university_api.classroom.model.Classroom;
import com.devland.university_api.classroom.model.dto.ClassroomRequestDTO;
import com.devland.university_api.classroom.model.dto.ClassroomResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("classrooms")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<Page<ClassroomResponseDTO>> getAll(
            @RequestParam("classname") Optional<String> optionalClassName,
            @RequestParam(value = "sort", defaultValue = "ASC") String sortString,
            @RequestParam(value = "order_by", defaultValue = "id") String orderBy,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Sort sort = Sort.by(Sort.Direction.valueOf(sortString), "id");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Classroom> pageClassroom = this.classroomService.getAll(optionalClassName, pageable);
        Page<ClassroomResponseDTO> classroomResponseDTOs = pageClassroom.map(Classroom::convertToResponse);

        return ResponseEntity.ok(classroomResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> getOne(@PathVariable("id") int id) {
        Classroom existingClassroom = this.classroomService.getOne(id);
        ClassroomResponseDTO classroomResponseDTO = existingClassroom.convertToResponse();

        return ResponseEntity.ok(classroomResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> create(@RequestBody @Valid ClassroomRequestDTO classroomRequestDTO) {
        Classroom newClassroom = classroomRequestDTO.convertToEntity();
        
        Classroom savedClassroom = this.classroomService.create(newClassroom);
        ClassroomResponseDTO classroomResponseDTO = savedClassroom.convertToResponse();

        return ResponseEntity.status(HttpStatus.CREATED).body(classroomResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> update(@PathVariable("id") int id, @RequestBody ClassroomRequestDTO classroomRequestDTO) {
        Classroom updatedClassroom = classroomRequestDTO.convertToEntity();
        updatedClassroom.setId(id);
        Classroom savedClassroom = this.classroomService.update(updatedClassroom);
        ClassroomResponseDTO classroomResponseDTO = savedClassroom.convertToResponse();

        return ResponseEntity.ok(classroomResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.classroomService.delete(id);

        return ResponseEntity.ok().build();
    }

}
