package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ClientRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ClientResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Client;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.ClientMapper;
import com.matiasbarraza.ordermanagementapi.repositories.ClientRepository;
import com.matiasbarraza.ordermanagementapi.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponse> search(String search, Pageable pageable) {
        Page<Client> clients;

        if (search == null || search.isBlank()) {
            clients = repository.findAll(pageable);
        } else {
            clients = repository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentNumberContainingIgnoreCase(
                            search,
                            search,
                            search,
                            pageable);
        }

        return clients.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client with id " + id + " not found"));

        return mapper.toResponse(client);
    }

    @Override
    @Transactional
    public ClientResponse create(ClientRequest request) {
        if (repository.existsByDocumentTypeAndDocumentNumber(
                request.documentType(),
                request.documentNumber()
        )) {
            throw new IllegalStateException("A client with this document already exists");
        }

        Client client = mapper.toEntity(request);
        Client savedClient = repository.save(client);
        return mapper.toResponse(savedClient);
    }

    @Override
    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client with id " + id + " not found"));

        if (repository.existsByDocumentTypeAndDocumentNumberAndIdNot(
                request.documentType(),
                request.documentNumber(),
                id)) {
            throw new IllegalStateException("A client with this document already exists");
        }

//        Client updatedClient = mapper.toEntity(request);
//        updatedClient.setId(client.getId());
//        Client savedClient = repository.save(updatedClient);
//        return mapper.toResponse(savedClient);

//      Updates the existing managed entity.
//      No repository.save() is required here because the entity was
//      loaded inside the current @Transactional context.
//      JPA/Hibernate detects the modified fields through dirty checking
//      and generates the UPDATE statement automatically at transaction commit.
        mapper.updateEntity(request, client);
        return mapper.toResponse(client);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Client with id " + id + " not found");
        }
        repository.deleteById(id);
    }

}