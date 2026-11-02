package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UserEntityRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must not exceed {max} characters")
        @Schema(description = "User email", example = "matias.barraza@email.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 200, message = "Password must be between {min} and {max} characters")
        @Schema(description = "User password", example = "Password1234")
        String password,

        @NotNull(message = "Active state is required")
        @Schema(description = "Whether the user is active", example = "true")
        Boolean active,

        @NotEmpty(message = "At least one role is required")
        @Schema(description = "Set of role names", example = "[\"USER\", \"ADMIN\"]")
        Set<String> roles

) {
}
