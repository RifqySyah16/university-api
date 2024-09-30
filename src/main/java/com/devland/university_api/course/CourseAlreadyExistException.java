package com.devland.university_api.course;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CourseAlreadyExistException extends RuntimeException {

    public CourseAlreadyExistException(String message) {
        super(message);
    }
}
