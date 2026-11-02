package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderDetailRequest(

        @NotNull(message = "Product id is required")
        @Positive(message = "Product id must be greater than zero")
        @Schema(description = "ID of the product associated with this detail", example = "1")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        @Schema(description = "Quantity of the product ordered", example = "1")
        Integer quantity

) {
}