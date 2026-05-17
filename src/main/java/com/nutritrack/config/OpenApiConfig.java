package com.nutritrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        return new OpenAPI()

                .info(
                        new Info()
                                .title("NutriTrack API")
                                .version("1.0.0")
                                .description(
                                        "API REST para NutriTrack - Sistema de seguimiento nutricional con IA. " +
                                        "Incluye autenticación JWT, gestión de pacientes y nutricionistas, " +
                                        "registro de comidas y hábitos, y asistente de IA."
                                )
                                .contact(
                                        new Contact()
                                                .name("NutriTrack Team")
                                                .email("contact@nutritrack.com")
                                )
                                .license(
                                        new License()
                                                .name("MIT License")
                                                .url("https://opensource.org/licenses/MIT")
                                )
                )
                .servers(List.of(localServer))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("JWT token for authentication. Format: Bearer {token}")
                                )
                );
    }
}