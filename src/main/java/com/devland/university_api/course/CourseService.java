package com.devland.university_api.course;

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

import com.devland.university_api.course.model.Course;
import com.devland.university_api.course.model.dto.CourseRequestDTO;
import com.devland.university_api.course.model.dto.CourseResponseDTO;
import com.devland.university_api.file.InvalidFileException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Page<Course> getAll(Optional<String> optionalCourseName, Pageable pageable) {
        if (optionalCourseName.isPresent()) {
            return this.courseRepository.findAllByCourseNameContainsIgnoreCase(optionalCourseName.get(), pageable);
        }

        return this.courseRepository.findAll(pageable);
    }

    public Course getOne(int id) {
        return this.courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
    }

    public Course create(Course newCourse) {
        Optional<Course> existingCourse = this.courseRepository.findByCourseName(newCourse.getCourseName());
        if (existingCourse.isPresent()) {
            throw new CourseAlreadyExistException("Course Already Exist");
        }

        return this.courseRepository.save(newCourse);
    }

    public Course update(Course updatedCourse) {
        Course existingCourse = this.getOne(updatedCourse.getId());
        updatedCourse.setId(existingCourse.getId());

        return this.courseRepository.save(updatedCourse);
    }

    public void delete(int id) {
        Course existingCourse = this.getOne(id);
        this.courseRepository.deleteById(existingCourse.getId());
    }

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Course> courses = this.courseRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Courses Info");
        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Course ID");
        row.createCell(1).setCellValue("Course Name");
        row.createCell(2).setCellValue("Course Email");
        row.createCell(3).setCellValue("Course Address");

        int dataRowIndex = 1;

        for (Course course : courses) {
            XSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(course.getId());
            dataRow.createCell(1).setCellValue(course.getCourseName());
            dataRow.createCell(2).setCellValue(course.getDescription());
            dataRowIndex++;
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    public List<CourseResponseDTO> saveCourseFromExcel(MultipartFile file) throws IOException {
        validateFile(file);

        List<CourseResponseDTO> courseResponseDTOs = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }

            CourseRequestDTO courseRequestDTO = createCourseRequestDTO(row);
            Course newCourse = courseRequestDTO.convertToEntity();
            Course savedCourse = this.create(newCourse);
            courseResponseDTOs.add(savedCourse.convertToResponse());
        }

        workbook.close();
        return courseResponseDTOs;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Please upload a valid Excel file.");
        }
    }

    private boolean isRowEmpty(Row row) {
        return row == null || row.getCell(1) == null || row.getCell(2) == null;
    }

    private CourseRequestDTO createCourseRequestDTO(Row row) {
        String courseName = row.getCell(1).getStringCellValue();
        String description = row.getCell(2).getStringCellValue();

        return new CourseRequestDTO(courseName, description);
    }
}
