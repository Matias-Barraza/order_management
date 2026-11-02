package com.matiasbarraza.ordermanagementapi.repositories;

import com.matiasbarraza.ordermanagementapi.domain.entities.Client;
import com.matiasbarraza.ordermanagementapi.domain.enums.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByDocumentTypeAndDocumentNumber(
            DocumentType documentType,
            String documentNumber
    );

    boolean existsByDocumentTypeAndDocumentNumberAndIdNot(
            DocumentType documentType,
            String documentNumber,
            Long id
    );

    Page<Client> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentNumberContainingIgnoreCase(
            String firstName,
            String lastName,
            String documentNumber,
            Pageable pageable
    );
}