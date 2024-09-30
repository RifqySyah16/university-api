package com.devland.university_api.applicationuser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrasionResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String photoPath;
    private String password;
}
