package com.matiasbarraza.ordermanagementapi.services;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UnitRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UnitResponse;

import java.util.List;

public interface UnitService {

    List<UnitResponse> findAll();

    UnitResponse findById(String id);

    UnitResponse create(UnitRequest request);

    UnitResponse update(String id, UnitRequest request);

    void delete(String id);
}