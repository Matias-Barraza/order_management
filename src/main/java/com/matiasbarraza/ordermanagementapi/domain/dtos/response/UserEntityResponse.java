package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UserEntityResponse(
        @Schema(description = "User id", example = "1")
        Long id,

        @Schema(description = "User email", example = "matias.barraza@email.com")
        String email,

        @Schema(description = "Whether the user is active", example = "true")
        boolean active,

        @Schema(description = "Assigned roles", example = "[\"USER\", \"ADMIN\"]")
        Set<String> roles
) {}