package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.AddressRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ClientRequest;
import com.matiasbarraza.ordermanagementapi.domain.entities.Client;
import com.matiasbarraza.ordermanagementapi.domain.enums.DocumentType;
import com.matiasbarraza.ordermanagementapi.mappers.ClientMapper;
import com.matiasbarraza.ordermanagementapi.repositories.ClientRepository;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.AddressResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ClientResponse;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void createShouldThrowExceptionWhenDocumentAlreadyExists() {

        // Arrange
        ClientRequest request = new ClientRequest(
                "Matias",
                "Barraza",
                DocumentType.DNI,
                "35123456",
                "+5491112345678",
                "matias.barraza@email.com",
                new AddressRequest(
                        "Av. Corrientes",
                        "1234",
                        "Moreno",
                        "Buenos Aires",
                        "1744",
                        "Argentina"
                )
        );

        when(repository.existsByDocumentTypeAndDocumentNumber(
                DocumentType.DNI,
                "35123456"
        )).thenReturn(true);

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> clientService.create(request)
        );

        // The duplicate client must not be saved.
        verify(repository, never()).save(any(Client.class));

        // The mapper should not be used after detecting the duplicate.
        verifyNoInteractions(mapper);
    }


    @Test
    void findByIdShouldReturnClientWhenItExists() {

        // Arrange
        Client client = new Client();
        client.setId(1L);

        AddressResponse address = new AddressResponse(
                "Av. Corrientes",
                "1234",
                "Moreno",
                "Buenos Aires",
                "1744",
                "Argentina"
        );

        ClientResponse expected = new ClientResponse(
                1L,
                "Matias",
                "Barraza",
                DocumentType.DNI,
                "35123456",
                "+5491112345678",
                "matias.barraza@email.com",
                address
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(client));

        when(mapper.toResponse(client))
                .thenReturn(expected);

        // Act
        ClientResponse result = clientService.findById(1L);

        // Assert
        assertSame(expected, result);

        verify(repository).findById(1L);
        verify(mapper).toResponse(client);
    }


    @Test
    void findByIdShouldThrowExceptionWhenClientDoesNotExist() {

        // Arrange
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.findById(99L)
        );

        verifyNoInteractions(mapper);
    }


    @Test
    void deleteShouldDeleteClientWhenItExists() {

        // Arrange
        when(repository.existsById(1L))
                .thenReturn(true);

        // Act
        clientService.delete(1L);

        // Assert
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }


    @Test
    void deleteShouldThrowExceptionWhenClientDoesNotExist() {

        // Arrange
        when(repository.existsById(99L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.delete(99L)
        );

        verify(repository, never()).deleteById(99L);
    }


}