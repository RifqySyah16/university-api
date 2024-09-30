package com.devland.university_api.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devland.university_api.file.InvalidFileException;
import com.devland.university_api.student.model.Student;
import com.devland.university_api.student.model.dto.StudentRequestDTO;
import com.devland.university_api.student.model.dto.StudentResponseDTO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Page<Student> getAll(Optional<String> optionalName, Pageable pageable) {
        if (optionalName.isPresent()) {
            return this.studentRepository.findAllByNameContainsIgnoreCase(optionalName.get(), pageable);
        }

        return this.studentRepository.findAll(pageable);
    }

    public Student getOne(int id) {
        return this.studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student ID Not Found"));
    }

    public Student create(Student newStudent) {
        Optional<Student> existingStudent = this.studentRepository.findByName(newStudent.getName());
        if (existingStudent.isPresent()) {
            throw new StudentAlreadyExistException("Student Already Exist");
        }

        return this.studentRepository.save(newStudent);
    }

    public Student update(Student updatedStudent) {
        Student existingStudent = this.getOne(updatedStudent.getId());
        updatedStudent.setId(existingStudent.getId());

        return this.studentRepository.save(updatedStudent);
    }

    public void delete(int id) {
        Student existingStudent = this.getOne(id);
        this.studentRepository.deleteById(existingStudent.getId());
    }

    public List<Student> findAllIn(List<Student> students) {
        List<Student> existingStudents = new ArrayList<>();
        for (Student student : students) {
            Student existingStudent = this.getOne(student.getId());
            existingStudents.add(existingStudent);
        }

        return existingStudents;
    }

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Student> students = this.studentRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Students Info");
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Student ID");
        row.createCell(1).setCellValue("Student Name");
        row.createCell(2).setCellValue("Student Email");
        row.createCell(3).setCellValue("Student Address");

        int dataRowIndex = 1;

        for (Student student : students) {
            XSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(student.getId());
            dataRow.createCell(1).setCellValue(student.getName());
            dataRow.createCell(2).setCellValue(student.getEmail());
            dataRow.createCell(3).setCellValue(student.getAddress());
            dataRowIndex++;
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    public List<StudentResponseDTO> saveStudentsFromExcel(MultipartFile file) throws IOException {
        validateFile(file);

        List<StudentResponseDTO> studentResponseDTOs = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }

            StudentRequestDTO studentRequestDTO = createStudentRequestDTO(row);
            Student newStudent = studentRequestDTO.convertToEntity();
            Student savedStudent = this.create(newStudent);
            studentResponseDTOs.add(savedStudent.convertToResponse());
        }

        workbook.close();
        return studentResponseDTOs;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Please upload a valid Excel file.");
        }
    }

    private boolean isRowEmpty(Row row) {
        return row == null || row.getCell(1) == null || row.getCell(2) == null;
    }

    private StudentRequestDTO createStudentRequestDTO(Row row) {
        String name = row.getCell(1).getStringCellValue();
        String email = row.getCell(2).getStringCellValue();
        String address = row.getCell(3).getStringCellValue();

        return new StudentRequestDTO(name, email, address);
    }

}
