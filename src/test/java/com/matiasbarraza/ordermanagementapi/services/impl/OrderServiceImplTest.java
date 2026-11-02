package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderDetailRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.BillingDocumentType;
import com.matiasbarraza.ordermanagementapi.domain.entities.Client;
import com.matiasbarraza.ordermanagementapi.domain.entities.Order;
import com.matiasbarraza.ordermanagementapi.domain.entities.OrderDetail;
import com.matiasbarraza.ordermanagementapi.domain.entities.Product;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.OrderMapper;
import com.matiasbarraza.ordermanagementapi.repositories.BillingDocumentTypeRepository;
import com.matiasbarraza.ordermanagementapi.repositories.ClientRepository;
import com.matiasbarraza.ordermanagementapi.repositories.OrderRepository;
import com.matiasbarraza.ordermanagementapi.repositories.ProductRepository;
import org.springframework.data.domain.PageImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BillingDocumentTypeRepository billingDocumentTypeRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void createShouldCalculateSubtotalsAndTotalCorrectly() {

        // Arrange: prepare the request and its dependencies.
        OrderRequest request = new OrderRequest(
                1L,
                "01",
                "F001",
                1001,
                List.of(
                        new OrderDetailRequest(10L, 2),
                        new OrderDetailRequest(20L, 3)
                )
        );

        LocalDate today = LocalDate.now();

        Client client = new Client();
        client.setId(1L);

        BillingDocumentType billingType = new BillingDocumentType();
        billingType.setCode("01");
        billingType.setDescription("Factura");

        Product product1 = new Product();
        product1.setId(10L);
        product1.setUnitPrice(new BigDecimal("150.00"));

        Product product2 = new Product();
        product2.setId(20L);
        product2.setUnitPrice(new BigDecimal("25.50"));

        Order order = new Order();
        order.setSerie("F001");
        order.setCorrelative(1001);

        OrderResponse expectedResponse = new OrderResponse(
                1L,
                today,
                1L,
                "Test Client",
                "01",
                "Factura",
                "F001",
                1001,
                new BigDecimal("376.50"),
                List.of()
        );

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(billingDocumentTypeRepository.findById("01"))
                .thenReturn(Optional.of(billingType));

        when(mapper.toEntity(request))
                .thenReturn(order);

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product1));

        when(productRepository.findById(20L))
                .thenReturn(Optional.of(product2));

        when(repository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toResponse(any(Order.class)))
                .thenReturn(expectedResponse);


        // Act: execute the business operation.
        OrderResponse response = orderService.create(request);


        // Assert: verify the saved order and its calculations.
        ArgumentCaptor<Order> orderCaptor =
                ArgumentCaptor.forClass(Order.class);

        verify(repository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertEquals(
                new BigDecimal("300.00"),
                savedOrder.getDetails().get(0).getSubtotal()
        );

        assertEquals(
                new BigDecimal("76.50"),
                savedOrder.getDetails().get(1).getSubtotal()
        );

        assertEquals(
                new BigDecimal("376.50"),
                savedOrder.getTotal()
        );

        assertEquals(
                new BigDecimal("150.00"),
                savedOrder.getDetails().get(0).getUnitPrice()
        );

        assertSame(
                savedOrder,
                savedOrder.getDetails().get(0).getOrder()
        );

        assertEquals(today, savedOrder.getDate());

        assertSame(expectedResponse, response);
    }


    @Test
    void createShouldThrowExceptionWhenProductDoesNotExist() {

        // Arrange
        OrderRequest request = new OrderRequest(
                1L,
                "01",
                "F001",
                1001,
                List.of(new OrderDetailRequest(99L, 2))
        );

        Client client = new Client();
        client.setId(1L);

        BillingDocumentType billingType = new BillingDocumentType();
        billingType.setCode("01");
        billingType.setDescription("Factura");

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(billingDocumentTypeRepository.findById("01"))
                .thenReturn(Optional.of(billingType));

        when(mapper.toEntity(request))
                .thenReturn(new Order());

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());


        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.create(request)
        );

        verify(repository, never()).save(any(Order.class));

        verify(mapper, never()).toResponse(any(Order.class));
    }


    @Test
    void searchShouldReturnEmptyPageWhenSearchIsNotNumeric() {

        // Arrange
        Pageable pageable = Pageable.ofSize(10);

        // Act
        Page<OrderResponse> result =
                orderService.search("abc", pageable);

        // Assert
        assertTrue(result.isEmpty());

        assertEquals(0, result.getTotalElements());

        verifyNoInteractions(repository, mapper);
    }


    @Test
    void searchShouldFindOrderWhenCorrelativeIsNumeric() {

        // Arrange
        Pageable pageable = Pageable.ofSize(10);

        Order order = new Order();

        OrderResponse expectedResponse = new OrderResponse(
                1L,
                LocalDate.now(),
                1L,
                "Test Client",
                "01",
                "Factura",
                "F001",
                1001,
                new BigDecimal("376.50"),
                List.of()
        );

        Page<Order> ordersPage = new PageImpl<>(
                List.of(order),
                pageable,
                1
        );

        when(repository.findByCorrelative(1001, pageable))
                .thenReturn(ordersPage);

        when(mapper.toResponse(order))
                .thenReturn(expectedResponse);

        // Act
        Page<OrderResponse> result =
                orderService.search("1001", pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(expectedResponse, result.getContent().get(0));

        verify(repository).findByCorrelative(1001, pageable);

        verify(repository, never()).findAll(any(Pageable.class));
    }


    @Test
    void updateShouldRecalculateSubtotalAndTotal() {

        // Arrange
        Long orderId = 1L;

        OrderRequest request = new OrderRequest(
                1L,
                "01",
                "F001",
                1001,
                List.of(new OrderDetailRequest(10L, 2))
        );

        Client client = new Client();
        client.setId(1L);

        BillingDocumentType billingType = new BillingDocumentType();
        billingType.setCode("01");
        billingType.setDescription("Factura");

        Product product = new Product();
        product.setId(10L);
        product.setUnitPrice(new BigDecimal("200.00"));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setTotal(new BigDecimal("100.00"));
        existingOrder.setDetails(new ArrayList<>());

        when(repository.findById(orderId))
                .thenReturn(Optional.of(existingOrder));

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(billingDocumentTypeRepository.findById("01"))
                .thenReturn(Optional.of(billingType));

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(repository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse expectedResponse = new OrderResponse(
                orderId,
                LocalDate.now(),
                1L,
                "Test Client",
                "01",
                "Factura",
                "F001",
                1001,
                new BigDecimal("400.00"),
                List.of()
        );

        when(mapper.toResponse(existingOrder))
                .thenReturn(expectedResponse);

        // Act
        OrderResponse response = orderService.update(orderId, request);

        // Assert
        assertEquals(
                new BigDecimal("400.00"),
                existingOrder.getDetails().get(0).getSubtotal()
        );

        assertEquals(
                new BigDecimal("400.00"),
                existingOrder.getTotal()
        );

        assertEquals(
                new BigDecimal("200.00"),
                existingOrder.getDetails().get(0).getUnitPrice()
        );

        assertSame(
                existingOrder,
                existingOrder.getDetails().get(0).getOrder()
        );

        verify(repository).save(existingOrder);

        assertSame(expectedResponse, response);
    }


    @Test
    void updateShouldThrowExceptionWhenOrderDoesNotExist() {

        // Arrange
        Long orderId = 99L;

        OrderRequest request = new OrderRequest(
                1L,
                "01",
                "F001",
                1001,
                List.of(new OrderDetailRequest(10L, 2))
        );

        when(repository.findById(orderId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.update(orderId, request)
        );

        // Verify that no order was saved.
        verify(repository, never()).save(any(Order.class));

        // Verify that no other dependency was called.
        verifyNoInteractions(
                clientRepository,
                billingDocumentTypeRepository,
                productRepository,
                mapper
        );
    }


    @Test
    void createShouldThrowExceptionWhenClientDoesNotExist() {

        // Arrange
        OrderRequest request = new OrderRequest(
                99L,
                "01",
                "F001",
                1001,
                List.of(new OrderDetailRequest(10L, 2))
        );

        when(clientRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.create(request)
        );

        // Verify that the order is not saved.
        verify(repository, never()).save(any(Order.class));

        // The operation should stop before accessing other dependencies.
        verifyNoInteractions(
                billingDocumentTypeRepository,
                productRepository,
                mapper
        );
    }


    @Test
    void createShouldThrowExceptionWhenBillingDocumentTypeDoesNotExist() {

        // Arrange
        OrderRequest request = new OrderRequest(
                1L,
                "99",
                "F001",
                1001,
                List.of(new OrderDetailRequest(10L, 2))
        );

        Client client = new Client();
        client.setId(1L);

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(billingDocumentTypeRepository.findById("99"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.create(request)
        );

        // The order must not be saved.
        verify(repository, never()).save(any(Order.class));

        // Processing must stop before mapping or looking up products.
        verifyNoInteractions(productRepository, mapper);
    }



}