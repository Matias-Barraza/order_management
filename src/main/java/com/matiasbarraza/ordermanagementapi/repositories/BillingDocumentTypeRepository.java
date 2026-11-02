package com.matiasbarraza.ordermanagementapi.repositories;

import com.matiasbarraza.ordermanagementapi.domain.entities.BillingDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingDocumentTypeRepository extends JpaRepository<BillingDocumentType, String > {

    Optional<BillingDocumentType> findByCode(String code);

}
