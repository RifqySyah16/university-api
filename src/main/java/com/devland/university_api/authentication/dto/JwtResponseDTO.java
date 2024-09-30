package com.devland.university_api.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";

    public JwtResponseDTO(String jwt) {
        this.token = jwt;
    }
}
