package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Code is required")
        @Size(max = 50, message = "Code must not exceed {max} characters")
        @Schema(description = "Code for the product", example = "prod-151")
        String code,

        @NotBlank(message = "Product name is required")
        @Size(max = 70, message = "Product name must not exceed {max} characters")
        @Schema(description = "Name for the product", example = "sugar")
        String name,

        @NotNull(message = "Unit price is required")
        @Positive(message = "Unit price must be greater than zero")
        @Schema(description = "Unit price for the product", example = "1500.00")
        BigDecimal unitPrice,

        @NotBlank(message = "Unit id is required")
        @Size(max = 3, message = "Unit id must be at most {max} characters")
        @Schema(description = "Unit identifier", example = "KG")
        String unitId
) {
}
