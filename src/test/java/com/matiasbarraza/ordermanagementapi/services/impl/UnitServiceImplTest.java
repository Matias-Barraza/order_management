package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UnitRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UnitResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.UnitMapper;
import com.matiasbarraza.ordermanagementapi.repositories.UnitRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

    @Mock
    private UnitRepository repository;

    @Mock
    private UnitMapper mapper;

    @InjectMocks
    private UnitServiceImpl unitService;


    @Test
    void findAllShouldReturnMappedUnits() {

        // Arrange
        Unit unit = new Unit("kgs", "kilograms");
        UnitResponse expected = new UnitResponse("kgs", "kilograms");

        when(repository.findAll()).thenReturn(List.of(unit));
        when(mapper.toResponse(unit)).thenReturn(expected);

        // Act
        List<UnitResponse> result = unitService.findAll();

        // Assert
        assertEquals(List.of(expected), result);
        verify(mapper).toResponse(unit);
    }


    @Test
    void findByIdShouldReturnUnitWhenItExists() {

        // Arrange
        Unit unit = new Unit("kgs", "kilograms");
        UnitResponse expected = new UnitResponse("kgs", "kilograms");

        when(repository.findById("kgs")).thenReturn(Optional.of(unit));
        when(mapper.toResponse(unit)).thenReturn(expected);

        // Act
        UnitResponse result = unitService.findById("kgs");

        // Assert
        assertEquals(expected, result);
    }


    @Test
    void findByIdShouldThrowExceptionWhenUnitDoesNotExist() {

        // Arrange
        when(repository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> unitService.findById("xyz")
        );

        verifyNoInteractions(mapper);
    }


    @Test
    void createShouldSaveAndReturnUnit() {

        // Arrange
        UnitRequest request = new UnitRequest("kgs", "kilograms");
        Unit unit = new Unit("kgs", "kilograms");
        UnitResponse expected = new UnitResponse("kgs", "kilograms");

        when(mapper.toEntity(request)).thenReturn(unit);
        when(repository.save(unit)).thenReturn(unit);
        when(mapper.toResponse(unit)).thenReturn(expected);

        // Act
        UnitResponse result = unitService.create(request);

        // Assert
        assertEquals(expected, result);
        verify(repository).save(unit);
    }


    @Test
    void updateShouldChangeDescriptionWhenUnitExists() {

        // Arrange
        Unit unit = new Unit("kgs", "kilograms");
        UnitRequest request = new UnitRequest("kgs", "kilograms updated");
        UnitResponse expected = new UnitResponse("kgs", "kilograms updated");

        when(repository.findById("kgs")).thenReturn(Optional.of(unit));
        when(mapper.toResponse(unit)).thenReturn(expected);

        // Act
        UnitResponse result = unitService.update("kgs", request);

        // Assert
        assertEquals("kilograms updated", unit.getDescription());
        assertEquals(expected, result);

        // The service updates the managed entity within its transaction.
        verify(repository, never()).save(any(Unit.class));
    }


    @Test
    void updateShouldThrowExceptionWhenUnitDoesNotExist() {

        // Arrange
        UnitRequest request = new UnitRequest("xyz", "unknown unit");

        when(repository.findById("xyz")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> unitService.update("xyz", request)
        );

        verifyNoInteractions(mapper);
    }


    @Test
    void deleteShouldDeleteUnitWhenItExists() {

        // Arrange
        when(repository.existsById("kgs")).thenReturn(true);

        // Act
        unitService.delete("kgs");

        // Assert
        verify(repository).deleteById("kgs");
    }


    @Test
    void deleteShouldThrowExceptionWhenUnitDoesNotExist() {

        // Arrange
        when(repository.existsById("xyz")).thenReturn(false);

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> unitService.delete("xyz")
        );

        verify(repository, never()).deleteById("xyz");
    }
}
