package com.matiasbarraza.ordermanagementapi.controllers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ProductRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ApiErrorResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ProductResponse;
import com.matiasbarraza.ordermanagementapi.services.ProductService;
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
@Tag(name = "Products", description = "Endpoints for managing products")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Search products")
    @ApiResponse(responseCode = "200", description = "Returns a paginated list of products")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @Parameter(description = "Optional case-insensitive search by product name or code", example = "sugar")
            @RequestParam(required = false) String search,
            @Parameter(description = "Zero-based page index. Defaults to 0", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page. Defaults to 5", example = "5") @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.search(search, pageable));
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @Parameter(description = "Unique identifier of the product", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")})
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
