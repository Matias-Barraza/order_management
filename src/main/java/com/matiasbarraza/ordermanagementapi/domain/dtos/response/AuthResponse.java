package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Response returned after successful user authentication")
public record AuthResponse(

        @Schema(description = "Unique identifier of the authenticated user", example = "1")
        long id,

        @Schema(description = "Email of the authenticated user", example = "admin@email.com")
        String email,

        @Schema(
                description = "Signed JWT access token. Use this token in the Authorization header with the Bearer scheme",
                example = "eyJhbGciOiJIUzI1NiJ9.example.signature")
        String token,

        @Schema(description = "Token validity duration in milliseconds", example = "86400000")
        long expiresIn

) {
}
