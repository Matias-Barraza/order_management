package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UnitRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UnitResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.UnitMapper;
import com.matiasbarraza.ordermanagementapi.repositories.UnitRepository;
import com.matiasbarraza.ordermanagementapi.services.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository repository;
    private final UnitMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse findById(String id) {
        Unit unit = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit not found with id: " + id));

        return mapper.toResponse(unit);
    }

    @Override
    @Transactional
    public UnitResponse create(UnitRequest request) {
        Unit unit = mapper.toEntity(request);

        Unit savedUnit = repository.save(unit);

        return mapper.toResponse(savedUnit);
    }

    @Override
    @Transactional
    public UnitResponse update(String id, UnitRequest request) {
        Unit unit = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit not found with id: " + id));

        unit.setDescription(request.description());

        return mapper.toResponse(unit);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Unit not found with id: " + id);
        }

        repository.deleteById(id);
    }

}