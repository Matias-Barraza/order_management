package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.AddressRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ClientRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.AddressResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ClientResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Address;
import com.matiasbarraza.ordermanagementapi.domain.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    ClientResponse toResponse(Client client);

    Client toEntity(ClientRequest clientRequest);

    AddressResponse toResponse(Address address);

    Address toEntity(AddressRequest addressRequest);

//     Updates an existing managed entity instead of creating a new one.
//     @MappingTarget tells MapStruct to modify the existing Client instance.
//     The entity is already managed by JPA inside the current transaction,
//     so Hibernate can automatically detect the changes through dirty checking
//     and execute the required UPDATE when the transaction is committed.
//     The ID is intentionally ignored because the entity identity must not be
//     changed during an update.
    @Mapping(target = "id", ignore = true)
    void updateEntity(ClientRequest request, @MappingTarget Client client);
}