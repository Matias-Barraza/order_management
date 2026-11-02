package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UnitRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UnitResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper {

    UnitResponse toResponse(Unit unit);

    Unit toEntity(UnitRequest unitRequest);

}
