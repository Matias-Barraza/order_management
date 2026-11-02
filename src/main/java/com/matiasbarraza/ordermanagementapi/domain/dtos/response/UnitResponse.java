package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UnitResponse(

        @Schema(description = "Unit identifier", example = "KG")
        String id,

        @Schema(description = "Description for the unit", example = "kilograms")
        String description
) {
}