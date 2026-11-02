package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UnitRequest(

        @NotBlank(message = "Unit is required")
        @Size(max = 3, message = "Unit must be at most {max} characters")
        @Schema(description = "Unit identifier", example = "KG")
        String id,

        @NotBlank(message = "Description is required")
        @Size(max = 50, message = "Description must not exceed {max} characters")
        @Schema(description = "Description for the unit", example = "kilograms")
        String description
) {
}