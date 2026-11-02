package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import com.matiasbarraza.ordermanagementapi.domain.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;

public record ClientResponse(
        @Schema(description = "Client id", example = "1")
        Long id,
        @Schema(description = "Client's first name", example = "Matias")
        String firstName,
        @Schema(description = "Client's last name", example = "Barraza")
        String lastName,
        @Schema(description = "Document type", example = "DNI")
        DocumentType documentType,
        @Schema(description = "Document number", example = "35123456")
        String documentNumber,
        @Schema(description = "Phone number", example = "+5491112345678")
        String phoneNumber,
        @Schema(description = "Client's email", example = "matias.barraza@email.com")
        String email,
        @Schema(description = "Client's address")
        AddressResponse address
) {}