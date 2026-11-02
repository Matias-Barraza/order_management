package com.matiasbarraza.ordermanagementapi.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Address {

    @Column(name = "street", length = 100, nullable = false)
    private String street;

    @Column(name = "number", length = 10, nullable = false)
    private String number;

    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @Column(name = "province", length = 50, nullable = false)
    private String province;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "country", length = 50, nullable = false)
    private String country;
}