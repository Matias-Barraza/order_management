package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ProductRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ProductResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "unitId", source = "unit.id")
    ProductResponse toResponse(Product product);

    @Mapping(target = "unit", ignore = true)
    Product toEntity(ProductRequest productRequest);

}