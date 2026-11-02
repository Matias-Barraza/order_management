package com.matiasbarraza.ordermanagementapi.services;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderResponse> search(String search, Pageable pageable);

    OrderResponse findById(Long id);

    OrderResponse create(OrderRequest request);

    OrderResponse update(Long id, OrderRequest request);

    void delete(Long id);

}
