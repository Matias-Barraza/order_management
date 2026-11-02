package com.matiasbarraza.ordermanagementapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "billing_document_type")
public class BillingDocumentType {

    @Id
    @Column(name = "code", length = 2)
    private String code;
    @Column(name = "description", nullable = false)
    private String description;


}

