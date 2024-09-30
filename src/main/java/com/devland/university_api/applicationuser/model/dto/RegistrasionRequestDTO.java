package com.devland.university_api.applicationuser.model.dto;

import com.devland.university_api.applicationuser.model.ApplicationUser;

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
public class RegistrasionRequestDTO {
    @Positive(message = "ID must be positive number or greater than zero")
    @NotNull(message = "ID is required")
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;

    public ApplicationUser convertToEntity() {
        return ApplicationUser.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .build();
    }
}
