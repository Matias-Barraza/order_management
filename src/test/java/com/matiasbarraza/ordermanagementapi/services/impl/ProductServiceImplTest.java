package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ProductRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ProductResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Product;
import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.ProductMapper;
import com.matiasbarraza.ordermanagementapi.repositories.ProductRepository;
import com.matiasbarraza.ordermanagementapi.repositories.UnitRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void createShouldAssignUnitAndSaveProduct() {

        // Arrange
        ProductRequest request = new ProductRequest(
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        Unit unit = new Unit("kgs", "kilograms");

        Product product = new Product();
        product.setCode("PROD-01");
        product.setName("Sugar");
        product.setUnitPrice(new BigDecimal("1500.00"));

        ProductResponse expectedResponse = new ProductResponse(
                1L,
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        when(productMapper.toEntity(request))
                .thenReturn(product);

        when(unitRepository.findById("kgs"))
                .thenReturn(Optional.of(unit));

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(expectedResponse);


        // Act
        ProductResponse response = productService.create(request);


        // Assert
        assertSame(unit, product.getUnit());

        assertSame(expectedResponse, response);

        verify(productRepository).save(product);
    }


    @Test
    void createShouldThrowExceptionWhenUnitDoesNotExist() {

        // Arrange
        ProductRequest request = new ProductRequest(
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        Product product = new Product();

        when(productMapper.toEntity(request))
                .thenReturn(product);

        when(unitRepository.findById("kgs"))
                .thenReturn(Optional.empty());


        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.create(request)
        );

        verify(productRepository, never())
                .save(any(Product.class));

        verify(productMapper, never())
                .toResponse(any(Product.class));
    }


    @Test
    void searchShouldReturnAllProductsWhenSearchIsBlank() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setId(1L);
        product.setCode("PROD-01");
        product.setName("Sugar");

        ProductResponse expected = new ProductResponse(
                1L,
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        Page<Product> productPage = new PageImpl<>(
                List.of(product),
                pageable,
                1
        );

        when(productRepository.findAll(pageable))
                .thenReturn(productPage);

        when(productMapper.toResponse(product))
                .thenReturn(expected);

        Page<ProductResponse> result =
                productService.search(" ", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(expected, result.getContent().get(0));

        verify(productRepository).findAll(pageable);
    }


    @Test
    void searchShouldFilterProductsWhenSearchIsProvided() {

        Pageable pageable = PageRequest.of(0, 10);

        Product product = new Product();
        product.setId(1L);
        product.setCode("PROD-01");
        product.setName("Sugar");

        ProductResponse expected = new ProductResponse(
                1L,
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        Page<Product> productPage = new PageImpl<>(
                List.of(product),
                pageable,
                1
        );

        when(productRepository
                .findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        "sug",
                        "sug",
                        pageable
                ))
                .thenReturn(productPage);

        when(productMapper.toResponse(product))
                .thenReturn(expected);

        Page<ProductResponse> result =
                productService.search("sug", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(expected, result.getContent().get(0));

        verify(productRepository)
                .findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        "sug",
                        "sug",
                        pageable
                );
    }


    @Test
    void findByIdShouldReturnProductWhenItExists() {

        Product product = new Product();
        product.setId(1L);
        product.setCode("PROD-01");
        product.setName("Sugar");

        ProductResponse expected = new ProductResponse(
                1L,
                "PROD-01",
                "Sugar",
                new BigDecimal("1500.00"),
                "kgs"
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(expected);

        ProductResponse result = productService.findById(1L);

        assertSame(expected, result);

        verify(productRepository).findById(1L);
        verify(productMapper).toResponse(product);
    }


    @Test
    void findByIdShouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(99L)
        );

        verifyNoInteractions(productMapper);
    }


    @Test
    void updateShouldModifyExistingProduct() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setCode("PROD-01");
        product.setName("Sugar");
        product.setUnitPrice(new BigDecimal("1500.00"));

        Unit unit = new Unit("kgs", "kilograms");

        ProductRequest request = new ProductRequest(
                "PROD-02",
                "Rice",
                new BigDecimal("2500.00"),
                "kgs"
        );

        ProductResponse expected = new ProductResponse(
                productId,
                "PROD-02",
                "Rice",
                new BigDecimal("2500.00"),
                "kgs"
        );

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(unitRepository.findById("kgs"))
                .thenReturn(Optional.of(unit));

        when(productMapper.toResponse(product))
                .thenReturn(expected);

        ProductResponse result = productService.update(productId, request);

        assertEquals("PROD-02", product.getCode());
        assertEquals("Rice", product.getName());
        assertEquals(
                new BigDecimal("2500.00"),
                product.getUnitPrice()
        );
        assertSame(unit, product.getUnit());

        assertSame(expected, result);

        verify(productRepository).findById(productId);
        verify(unitRepository).findById("kgs");

        // The managed entity is updated using JPA dirty checking.
        verify(productRepository, never()).save(any(Product.class));
    }



}