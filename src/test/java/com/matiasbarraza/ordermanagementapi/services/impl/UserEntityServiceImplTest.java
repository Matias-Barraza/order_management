package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UserEntityRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UserEntityResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Role;
import com.matiasbarraza.ordermanagementapi.domain.entities.UserEntity;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.UserEntityMapper;
import com.matiasbarraza.ordermanagementapi.repositories.RoleRepository;
import com.matiasbarraza.ordermanagementapi.repositories.UserEntityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {

    @Mock
    private UserEntityRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserEntityMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserEntityServiceImpl userService;


    @Test
    void createShouldEncodePasswordAndAssignRoles() {

        // Arrange
        UserEntityRequest request = new UserEntityRequest(
                "newuser@example.com",
                "Password1234",
                true,
                Set.of("USER")
        );

        UserEntity user = new UserEntity();

        Role role = new Role();
        role.setName("USER");

        UserEntityResponse expected = new UserEntityResponse(
                1L,
                "newuser@example.com",
                true,
                Set.of("USER")
        );

        when(repository.findByEmailIgnoreCase(request.email()))
                .thenReturn(Optional.empty());

        when(mapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        when(roleRepository.findByNameIgnoreCase("USER"))
                .thenReturn(Optional.of(role));

        when(repository.save(user))
                .thenAnswer(invocation -> {
                    user.setId(1L);
                    return user;
                });

        when(mapper.toResponse(user))
                .thenReturn(expected);

        // Act
        UserEntityResponse result = userService.create(request);

        // Assert
        assertSame(expected, result);
        assertEquals("encoded-password", user.getPassword());
        assertTrue(user.getRoles().contains(role));

        verify(passwordEncoder).encode(request.password());
        verify(repository).save(user);
    }


    @Test
    void createShouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        UserEntityRequest request = new UserEntityRequest(
                "existing@example.com",
                "Password1234",
                true,
                Set.of("USER")
        );

        when(repository.findByEmailIgnoreCase(request.email()))
                .thenReturn(Optional.of(new UserEntity()));

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> userService.create(request)
        );

        verify(repository, never()).save(any(UserEntity.class));
        verifyNoInteractions(mapper, roleRepository, passwordEncoder);
    }


    @Test
    void createShouldThrowExceptionWhenRoleDoesNotExist() {

        // Arrange
        UserEntityRequest request = new UserEntityRequest(
                "newuser@example.com",
                "Password1234",
                true,
                Set.of("UNKNOWN")
        );

        UserEntity user = new UserEntity();

        when(repository.findByEmailIgnoreCase(request.email()))
                .thenReturn(Optional.empty());

        when(mapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        when(roleRepository.findByNameIgnoreCase("UNKNOWN"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.create(request)
        );

        verify(repository, never()).save(any(UserEntity.class));
    }


    @Test
    void findByIdShouldReturnUserWhenItExists() {

        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@example.com");

        UserEntityResponse expected = new UserEntityResponse(
                1L,
                "user@example.com",
                true,
                Set.of("USER")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(mapper.toResponse(user))
                .thenReturn(expected);

        // Act
        UserEntityResponse result = userService.findById(1L);

        // Assert
        assertSame(expected, result);
        verify(mapper).toResponse(user);
    }


    @Test
    void findByIdShouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(99L)
        );

        verifyNoInteractions(mapper);
    }


    @Test
    void updateShouldThrowExceptionWhenEmailBelongsToAnotherUser() {

        // Arrange
        Long userId = 1L;

        UserEntityRequest request = new UserEntityRequest(
                "existing@example.com",
                "Password1234",
                true,
                Set.of("USER")
        );

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        when(repository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(repository.existsByEmailIgnoreCaseAndIdNot(
                request.email(),
                userId
        )).thenReturn(true);

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> userService.update(userId, request)
        );

        verifyNoInteractions(mapper, roleRepository, passwordEncoder);
    }


    @Test
    void deleteShouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        when(repository.existsById(99L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(99L)
        );

        verify(repository, never()).deleteById(99L);
    }
}
