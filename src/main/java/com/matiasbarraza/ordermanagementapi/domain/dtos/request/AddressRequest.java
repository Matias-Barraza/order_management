package com.matiasbarraza.ordermanagementapi.domain.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Street is required")
        @Size(max = 100, message = "Street must be max {max} characters")
        @Schema(description = "Street name", example = "Av. Corrientes")
        String street,

        @NotBlank(message = "Number is required")
        @Size(max = 10, message = "Number must be max {max} characters")
        @Schema(description = "Street number", example = "1234")
        String number,

        @NotBlank(message = "City is required")
        @Size(max = 50, message = "City must be max {max} characters")
        @Schema(description = "City", example = "Moreno")
        String city,

        @NotBlank(message = "Province is required")
        @Size(max = 50, message = "Province must be max {max} characters")
        @Schema(description = "Province", example = "Buenos Aires")
        String province,

        @Size(max = 10, message = "Postal code must be max {max} characters")
        @Schema(description = "Postal code", example = "1744")
        String postalCode,

        @NotBlank(message = "Country is required")
        @Size(max = 50, message = "Country must be max {max} characters")
        @Schema(description = "Country", example = "Argentina")
        String country
) {}