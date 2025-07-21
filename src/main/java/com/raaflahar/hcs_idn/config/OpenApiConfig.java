package com.raaflahar.hcs_idn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI openAPI() {
                final String bearerSchemeName = "bearerAuth";

                return new OpenAPI()
                                .addServersItem(new Server().url("http://localhost:8081")
                                                .description("Local Development Server"))
                                .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName))

                                .components(
                                                new Components()
                                                                .addSecuritySchemes(bearerSchemeName,
                                                                                new SecurityScheme()
                                                                                                .name(bearerSchemeName)
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")))
                                .info(new Info()
                                                .title("Hitachi Solution Indo")
                                                .description("Swagger Controller")
                                                .version("v1.0"));
        }
}
