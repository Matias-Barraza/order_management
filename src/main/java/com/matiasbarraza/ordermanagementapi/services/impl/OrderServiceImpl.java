package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Order;
import com.matiasbarraza.ordermanagementapi.domain.entities.OrderDetail;
import com.matiasbarraza.ordermanagementapi.exceptions.ResourceNotFoundException;
import com.matiasbarraza.ordermanagementapi.mappers.OrderMapper;
import com.matiasbarraza.ordermanagementapi.repositories.BillingDocumentTypeRepository;
import com.matiasbarraza.ordermanagementapi.repositories.ClientRepository;
import com.matiasbarraza.ordermanagementapi.repositories.OrderRepository;
import com.matiasbarraza.ordermanagementapi.repositories.ProductRepository;
import com.matiasbarraza.ordermanagementapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderDetailRequest;
import com.matiasbarraza.ordermanagementapi.domain.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ClientRepository clientRepository;
    private final BillingDocumentTypeRepository billingDocumentTypeRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> search(String search, Pageable pageable) {
        Page<Order> orders;

        if (search == null || search.isBlank()) {
            orders = repository.findAll(pageable);
        } else {
            try {
                Integer correlative = Integer.valueOf(search.trim());
                orders = repository.findByCorrelative(correlative, pageable);
            } catch (NumberFormatException ex) {
                orders = Page.empty(pageable);
            }
        }

        return orders.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order with id " + id + " not found"));

        return mapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {

        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        var billingType = billingDocumentTypeRepository
                .findById(request.billingDocumentTypeCode())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Billing Document Type not found"
                        ));

        Order order = mapper.toEntity(request);

        order.setDate(LocalDate.now());
        order.setClient(client);
        order.setBillingDocumentType(billingType);

        BigDecimal total = replaceOrderDetails(order, request.details());
        order.setTotal(total);

        Order savedOrder = repository.save(order);

        return mapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse update(Long id, OrderRequest request) {

        Order existingOrder = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order with id " + id + " not found"
                        ));

        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        var billingType = billingDocumentTypeRepository
                .findById(request.billingDocumentTypeCode())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Billing Document Type not found"
                        ));

        existingOrder.setClient(client);
        existingOrder.setBillingDocumentType(billingType);
        existingOrder.setSerie(request.serie());
        existingOrder.setCorrelative(request.correlative());

        BigDecimal total = replaceOrderDetails(
                existingOrder,
                request.details()
        );

        existingOrder.setTotal(total);

        Order savedOrder = repository.save(existingOrder);

        return mapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    private BigDecimal replaceOrderDetails(Order order, List<OrderDetailRequest> requestedDetails) {
        if (requestedDetails == null || requestedDetails.isEmpty()) {
            throw new IllegalArgumentException("Order details must not be empty");
        }

        if (order.getDetails() == null) {
            order.setDetails(new ArrayList<>());
        } else {
            order.getDetails().clear();
        }

        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetailRequest requestDetail : requestedDetails) {
            Product product = productRepository
                    .findById(requestDetail.productId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product with id " + requestDetail.productId() + " not found"));

            BigDecimal unitPrice = product.getUnitPrice();

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(requestDetail.quantity()));

            OrderDetail detail = new OrderDetail();

            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(requestDetail.quantity());
            detail.setUnitPrice(unitPrice);
            detail.setSubtotal(subtotal);

            order.getDetails().add(detail);

            total = total.add(subtotal);
        }

        return total;
    }
}