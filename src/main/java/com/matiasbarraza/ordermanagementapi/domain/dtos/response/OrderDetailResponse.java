package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record OrderDetailResponse(
        @Schema(description = "Position of the item within the order, starting at 1", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Name for the product", example = "sugar")
        String productName,

        @Schema(description = "Quantity for the product", example = "5")
        Integer quantity,

        @Schema(description = "Unit price for the product", example = "1500.00")
        BigDecimal unitPrice,

        @Schema(description = "Subtotal calculated for this item", example = "7500.00")
        BigDecimal subtotal
) {
}

