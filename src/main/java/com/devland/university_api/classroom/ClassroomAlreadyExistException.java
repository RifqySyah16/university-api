package com.devland.university_api.classroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ClassroomAlreadyExistException extends RuntimeException {

    public ClassroomAlreadyExistException(String message) {
        super(message);
    }
}
