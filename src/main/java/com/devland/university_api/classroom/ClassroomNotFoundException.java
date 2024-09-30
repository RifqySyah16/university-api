package com.devland.university_api.classroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ClassroomNotFoundException extends RuntimeException{

    public ClassroomNotFoundException(String message) {
        super(message);
    }
}
