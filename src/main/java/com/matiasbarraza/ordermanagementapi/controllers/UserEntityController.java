package com.matiasbarraza.ordermanagementapi.controllers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UserEntityRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ApiErrorResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UserEntityResponse;
import com.matiasbarraza.ordermanagementapi.services.UserEntityService;
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
@Tag(name = "Users", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserEntityController {

    private final UserEntityService service;

    @Operation(summary = "Search users")
    @ApiResponse(responseCode = "200", description = "Returns a paginated list of users")
    @GetMapping
    public ResponseEntity<Page<UserEntityResponse>> search(
            @Parameter(description = "Optional case-insensitive search by user email", example = "admin@email.com")
            @RequestParam(required = false) String search,
            @Parameter(description = "Zero-based page index. Defaults to 0", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page. Defaults to 5", example = "5") @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.search(search, pageable));
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserEntityResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<UserEntityResponse> findById(
            @Parameter(description = "Unique identifier of the user", example = "1") @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "A user with this email already exists")})
    @PostMapping
    public ResponseEntity<UserEntityResponse> create(@Valid @RequestBody UserEntityRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "A user with this email already exists")})
    @PutMapping("/{id}")
    public ResponseEntity<UserEntityResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody UserEntityRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
