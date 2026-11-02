package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderDetailRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderDetailResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderDetailResponse toResponse(OrderDetail orderDetail);

    @Mapping(source = "productId", target = "product.id")
    OrderDetail toEntity(OrderDetailRequest orderDetailRequest);
}

