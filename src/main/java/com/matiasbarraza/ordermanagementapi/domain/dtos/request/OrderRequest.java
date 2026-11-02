package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Client id is required")
        @Positive(message = "Client id must be greater than zero")
        @Schema(description = "ID of the client placing the order", example = "1")
        Long clientId,

        @NotBlank(message = "Billing document type code is required")
        @Size(max = 2, message = "Billing document type code must not exceed {max} characters")
        @Schema(description = "Code of the billing document type", example = "01")
        String billingDocumentTypeCode,

        @NotBlank(message = "Serie is required")
        @Size(max = 4, message = "Serie must not exceed {max} characters")
        @Schema(description = "Document series prefix", example = "F001")
        String serie,

        @NotNull(message = "Correlative is required")
        @Positive(message = "Correlative must be greater than zero")
        @Schema(description = "Document sequence number", example = "1001")
        Integer correlative,

        @NotEmpty(message = "Order details must not be empty")
        @Valid
        @Schema(description = "List of products and quantities included in the order")
        List<OrderDetailRequest> details
) {
}
