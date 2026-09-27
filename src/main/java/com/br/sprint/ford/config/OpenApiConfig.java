package com.br.sprint.ford.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA = "bearerAuth";

    @Bean
    public OpenAPI fordOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ford & FIAP - Inteligência Competitiva Automotiva")
                        .description("API para consulta padronizada de especificações técnicas de veículos "
                                + "concorrentes. Faça login em /api/v1/auth/login e use o token no botão Authorize.")
                        .version("3.0.0")
                        .contact(new Contact().name("FIAP - Equipe Challenge Ford")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA))
                .components(new Components().addSecuritySchemes(ESQUEMA,
                        new SecurityScheme()
                                .name(ESQUEMA)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}