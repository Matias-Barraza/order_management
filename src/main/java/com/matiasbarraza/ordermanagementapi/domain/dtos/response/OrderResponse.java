package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        @Schema(description = "Order ID", example = "250")
        Long id,

        @Schema(description = "Date when the order was registered", example = "2026-10-08")
        LocalDate date,

        @Schema(description = "Client ID", example = "102")
        Long clientId,

        @Schema(description = "Name of the client", example = "John Doe")
        String clientName,

        @Schema(description = "Code of the billing document type", example = "01")
        String billingDocumentTypeCode,

        @Schema(description = "Description of the billing document type", example = "Factura")
        String billingDocumentTypeDescription,

        @Schema(description = "Document series prefix", example = "F001")
        String serie,

        @Schema(description = "Correlative number of the document", example = "45")
        Integer correlative,

        @Schema(description = "Total cost of the order", example = "7500.00")
        BigDecimal total,

        @Schema(description = "List of item details within this order")
        List<OrderDetailResponse> details
) {
}
