package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ProductRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ProductResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Product;
import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.ProductMapper;
import com.matiasbarraza.ordermanagementapi.repositories.ProductRepository;
import com.matiasbarraza.ordermanagementapi.repositories.UnitRepository;
import com.matiasbarraza.ordermanagementapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String search, Pageable pageable) {

        Page<Product> products;

        if (search == null || search.isBlank()) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository
                    .findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
                            search,
                            search,
                            pageable
                    );
        }

        return products.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit not found with id: " + request.unitId()));

        product.setUnit(unit);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit not found with id: " + request.unitId()));

        product.setCode(request.code());
        product.setName(request.name());
        product.setUnitPrice(request.unitPrice());
        product.setUnit(unit);

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

}