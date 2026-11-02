package com.matiasbarraza.ordermanagementapi.controllers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UnitRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ApiErrorResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UnitResponse;
import com.matiasbarraza.ordermanagementapi.services.UnitService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:5173"})
@Tag(name = "Units", description = "Endpoints for retrieving and managing measurement units")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/units")
@PreAuthorize("isAuthenticated()")
public class UnitController {

    private final UnitService service;

    @Operation(summary = "Get all units")
    @ApiResponse(responseCode = "200", description = "Returns all units")
    @GetMapping
    public ResponseEntity<List<UnitResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Get a unit by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit found",
                    content = @Content(schema = @Schema(implementation = UnitResponse.class))),
            @ApiResponse(responseCode = "404", description = "Unit not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> findById(@Parameter(description = "Unit identifier", example = "KG")
                                                     @PathVariable String id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Create a new unit")
    @ApiResponse(responseCode = "201", description = "Unit created successfully")
    @PostMapping
    public ResponseEntity<UnitResponse> create(@Valid @RequestBody UnitRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(summary = "Update a unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit updated successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found")})
    @PutMapping("/{id}")
    public ResponseEntity<UnitResponse> update(@PathVariable String id,
                                               @Valid @RequestBody UnitRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Delete a unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unit deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
