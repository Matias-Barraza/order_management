package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressResponse(
        @Schema(description = "Street name", example = "Av. Corrientes")
        String street,
        @Schema(description = "Street number", example = "1234")
        String number,
        @Schema(description = "City", example = "Moreno")
        String city,
        @Schema(description = "Province", example = "Buenos Aires")
        String province,
        @Schema(description = "Postal code", example = "C1043")
        String postalCode,
        @Schema(description = "Country", example = "Argentina")
        String country
) {}