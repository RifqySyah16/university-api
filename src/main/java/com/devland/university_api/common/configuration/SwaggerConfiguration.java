package com.devland.university_api.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {
    @Bean
    OpenAPI app() {
        Info info = new Info();
        info.setTitle("Universitas Siber");
        info.description("Data Station");
        info.setVersion("1.0.0");

        return new OpenAPI().info(info);
    }
}
