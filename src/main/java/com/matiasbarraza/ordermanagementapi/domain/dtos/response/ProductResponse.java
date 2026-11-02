package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponse(

        @Schema(description = "Product id", example = "15")
        Long id,

        @Schema(description = "Code for the product", example = "prod-151")
        String code,

        @Schema(description = "Name for the product", example = "sugar")
        String name,

        @Schema(description = "Unit price for the product", example = "1500.00")
        BigDecimal unitPrice,

        @Schema(description = "Unit identifier", example = "KG")
        String unitId

) {
}
