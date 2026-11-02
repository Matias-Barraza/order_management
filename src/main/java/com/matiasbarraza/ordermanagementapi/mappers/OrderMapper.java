package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderDetailResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Order;
import com.matiasbarraza.ordermanagementapi.domain.entities.OrderDetail;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = OrderDetailMapper.class)
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(target = "clientName", expression = "java(order.getClient().getFirstName() + \" \" + order.getClient().getLastName())")
    @Mapping(source = "billingDocumentType.code", target = "billingDocumentTypeCode")
    @Mapping(source = "billingDocumentType.description", target = "billingDocumentTypeDescription")
    @Mapping(target = "details", source = "details")
    OrderResponse toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "billingDocumentType", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "details", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    @AfterMapping
    default void linkOrderDetails(@MappingTarget Order order) {
        if (order.getDetails() != null) {
            order.getDetails().forEach(detail -> detail.setOrder(order));
        }
    }

    default List<OrderDetailResponse> mapDetails(List<OrderDetail> details) {
        if (details == null) {
            return null;
        }

        List<OrderDetailResponse> list = new ArrayList<>(details.size());
        long lineNumber = 1;

        for (OrderDetail orderDetail : details) {
            list.add(new OrderDetailResponse(
                    lineNumber++,
                    orderDetail.getProduct() != null ? orderDetail.getProduct().getId() : null,
                    orderDetail.getProduct() != null ? orderDetail.getProduct().getName() : null,
                    orderDetail.getQuantity(),
                    orderDetail.getUnitPrice(),
                    orderDetail.getSubtotal()
            ));
        }

        return list;
    }
}
