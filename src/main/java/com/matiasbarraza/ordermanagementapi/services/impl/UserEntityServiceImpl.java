package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UserEntityRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UserEntityResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Role;
import com.matiasbarraza.ordermanagementapi.domain.entities.UserEntity;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.UserEntityMapper;
import com.matiasbarraza.ordermanagementapi.repositories.RoleRepository;
import com.matiasbarraza.ordermanagementapi.repositories.UserEntityRepository;
import com.matiasbarraza.ordermanagementapi.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {

    private final UserEntityRepository repository;
    private final RoleRepository roleRepository;
    private final UserEntityMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserEntityResponse> search(String search, Pageable pageable) {
        Page<UserEntity> users;

        if (search == null || search.isBlank()) {
            users = repository.findAll(pageable);
        } else {
            users = repository.findByEmailContainingIgnoreCase(search, pageable);
        }

        return users.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntityResponse findById(Long id) {
        UserEntity user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));

        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserEntityResponse create(UserEntityRequest request) {
        if (repository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new IllegalStateException("A user with this email already exists");
        }

        UserEntity user = mapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRoles(fetchRoles(request.roles()));

        UserEntity savedUser = repository.save(user);

        return mapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserEntityResponse update(Long id, UserEntityRequest request) {
        UserEntity user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));

        if (repository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new IllegalStateException("A user with this email already exists");
        }

        mapper.updateEntity(request, user);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        user.setRoles(fetchRoles(request.roles()));

        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    private Set<Role> fetchRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new IllegalArgumentException("At least one role is required");
        }

        return roleNames
                .stream()
                .map(roleName ->
                        roleRepository.findByNameIgnoreCase(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found")))
                .collect(Collectors.toSet());
    }

}