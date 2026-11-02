package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import com.matiasbarraza.ordermanagementapi.domain.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be max {max} characters")
        @Schema(description = "Client's first name", example = "Matias")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be max {max} characters")
        @Schema(description = "Client's last name", example = "Barraza")
        String lastName,

        @NotNull(message = "Document type is required")
        @Schema(description = "Document type", example = "DNI")
        DocumentType documentType,

        @NotBlank(message = "Document number is required")
        @Size(max = 15, message = "Document number must be max {max} characters")
        @Schema(description = "Document number", example = "35123456")
        String documentNumber,

        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must be max {max} characters")
        @Schema(description = "Phone number", example = "+5491112345678")
        String phoneNumber,

        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must be max {max} characters")
        @Schema(description = "Client's email", example = "matias.barraza@email.com")
        String email,

        @NotNull(message = "Address is required")
        @Valid
        @Schema(description = "Client's address")
        AddressRequest address
) {}