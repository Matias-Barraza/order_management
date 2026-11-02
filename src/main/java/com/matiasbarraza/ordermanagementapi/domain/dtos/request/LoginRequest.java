package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must not exceed {max} characters")
        @Schema(description = "User email", example = "admin@email.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 200, message = "Password must be between {min} and {max} characters")
        @Schema(description = "User password", example = "demo1234")
        String password

) {
}
