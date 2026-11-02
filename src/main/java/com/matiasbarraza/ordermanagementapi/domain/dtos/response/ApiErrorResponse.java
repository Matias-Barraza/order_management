package com.matiasbarraza.ordermanagementapi.domain.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Standardized API error response")
public record ApiErrorResponse(
        @Schema(description = "HTTP status code associated with the error", example = "400")
        int status,

        @Schema(description = "General description of the error", example = "Validation failed for one or more fields")
        String message,

        @Schema(description = "List of field-level validation errors. Empty when no specific field validation errors apply")
        List<FieldError> errors
) {

    public ApiErrorResponse {
        if (errors == null) {
            errors = List.of();
        }
    }

    @Schema(name = "ApiFieldError", description = "Information about an individual field validation error")
    public record FieldError(
            @Schema(description = "Name of the field that caused the validation error", example = "email")
            String field,

            @Schema(description = "Description of the validation error", example = "Email should be valid")
            String message
    ) {
    }
}
