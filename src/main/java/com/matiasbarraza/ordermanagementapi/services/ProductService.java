package com.matiasbarraza.ordermanagementapi.services;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ProductRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> search(String search, Pageable pageable);

    ProductResponse findById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}