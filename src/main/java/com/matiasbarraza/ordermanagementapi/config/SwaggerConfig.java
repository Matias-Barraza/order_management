package com.matiasbarraza.ordermanagementapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order Management API",
                description = """
                        REST API for managing clients, products, units and orders.
                        Includes JWT authentication, role-based authorization,
                        data validation and database versioning with Flyway.
                        """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Matias Barraza",
                        email = "matiasbarraza292@gmail.com",
                        url = "https://www.linkedin.com/in/matias--barraza/"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080/api",
                        description = "Local Docker Compose environment"
                )
        }
)

public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
