package com.matiasbarraza.ordermanagementapi.controllers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.OrderRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ApiErrorResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.OrderResponse;
import com.matiasbarraza.ordermanagementapi.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:5173"})
@Tag(name = "Orders", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Search orders")
    @ApiResponse(responseCode = "200", description = "Returns a paginated list of orders")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> search(
            @Parameter(description = "Optional exact document correlative number. Leave empty to retrieve all orders", example = "1001")
            @RequestParam(required = false) String search,
            @Parameter(description = "Zero-based page index. Defaults to 0", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page. Defaults to 5", example = "5") @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.search(search, pageable));
    }

    @Operation(summary = "Get order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @Parameter(description = "Unique identifier of the order", example = "1") @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")})
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Delete order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
