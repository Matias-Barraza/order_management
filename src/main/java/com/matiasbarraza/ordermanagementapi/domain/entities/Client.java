package com.matiasbarraza.ordermanagementapi.domain.entities;

import com.matiasbarraza.ordermanagementapi.domain.enums.DocumentType;
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
@Table(name = "clients", uniqueConstraints =
        {@UniqueConstraint(name = "uk_client_document", columnNames = {"document_type", "document_number"})})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 10, nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", length = 15, nullable = false)
    private String documentNumber;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Embedded
    private Address address;

}