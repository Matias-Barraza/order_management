package com.matiasbarraza.ordermanagementapi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
    DNI("DNI", 8),
    CUIT("CUIT", 11),
    PASSPORT("Passport", 15);

    private final String description;
    private final int maxLength;
}
