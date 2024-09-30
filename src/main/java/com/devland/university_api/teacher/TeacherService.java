package com.devland.university_api.teacher;

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
import com.devland.university_api.teacher.model.Teacher;
import com.devland.university_api.teacher.model.dto.TeacherRequestDTO;
import com.devland.university_api.teacher.model.dto.TeacherResponseDTO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public Page<Teacher> findAll(Optional<String> optionalName, Pageable pageable) {
        if (optionalName.isPresent()) {
            return this.teacherRepository.findAllByNameContainsIgnoreCase(optionalName.get(), pageable);
        }

        return this.teacherRepository.findAll(pageable);
    }

    public Teacher getOne(int id) {
        return this.teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher ID Not Found"));
    }

    public Teacher create(Teacher newTeacher) {
        Optional<Teacher> existingTeacher = this.teacherRepository.findByName(newTeacher.getName());
        if (existingTeacher.isPresent()) {
            throw new TeacherAlreadyExistException("Teacher Already Exist");
        }

        return this.teacherRepository.save(newTeacher);
    }

    public Teacher update(Teacher updatedTeacher) {
        Teacher existingTeacher = this.getOne(updatedTeacher.getId());
        updatedTeacher.setId(existingTeacher.getId());

        return this.teacherRepository.save(updatedTeacher);
    }

    public void delete(int id) {
        Teacher existingTeacher = this.getOne(id);
        this.teacherRepository.deleteById(existingTeacher.getId());
    }

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Teacher> teachers = this.teacherRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Teachers Info");
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Teacher ID");
        row.createCell(1).setCellValue("Teacher Name");
        row.createCell(2).setCellValue("Teacher Email");
        row.createCell(3).setCellValue("Teacher Address");

        int dataRowIndex = 1;

        for (Teacher teacher : teachers) {
            XSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(teacher.getId());
            dataRow.createCell(1).setCellValue(teacher.getName());
            dataRow.createCell(2).setCellValue(teacher.getEmail());
            dataRow.createCell(3).setCellValue(teacher.getAddress());
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

    public List<TeacherResponseDTO> saveTeachersFromExcel(MultipartFile file) throws IOException {
        validateFile(file);

        List<TeacherResponseDTO> teacherResponseDTOs = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }

            TeacherRequestDTO teacherRequestDTO = createTeacherRequestDTO(row);
            Teacher newTeacher = teacherRequestDTO.convertToEntity();
            Teacher savedTeacher = this.create(newTeacher);
            teacherResponseDTOs.add(savedTeacher.convertToResponse());
        }

        workbook.close();
        return teacherResponseDTOs;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Please upload a valid Excel file.");
        }
    }

    private boolean isRowEmpty(Row row) {
        return row == null || row.getCell(1) == null || row.getCell(2) == null;
    }

    private TeacherRequestDTO createTeacherRequestDTO(Row row) {
        String name = row.getCell(1).getStringCellValue();
        String email = row.getCell(2).getStringCellValue();
        String address = row.getCell(3).getStringCellValue();

        return new TeacherRequestDTO(name, email, address);
    }
}
