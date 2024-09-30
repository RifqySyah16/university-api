package com.devland.university_api.teacher.model.dto;

import com.devland.university_api.teacher.model.Teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequestDTO {
    @Positive(message = "ID must be positive number or greter than zero")
    @NotNull(message = "ID is required")
    private int id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    public TeacherRequestDTO(@NotBlank(message = "Name is required") String name,
            @NotBlank(message = "Email is required") String email,
            @NotBlank(message = "Address is required") String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Teacher convertToEntity() {
        return Teacher.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .build();
    }
}
