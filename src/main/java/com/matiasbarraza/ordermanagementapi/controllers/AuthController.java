package com.matiasbarraza.ordermanagementapi.controllers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.LoginRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ApiErrorResponse;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.AuthResponse;
import com.matiasbarraza.ordermanagementapi.services.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5173"})
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService service;

    @Operation(summary = "User login",
            description = """
                    Authenticates a user using email and password.
                    Returns a signed JWT access token that must be
                    included in the Authorization header when accessing
                    protected endpoints.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.login(request));
    }
}
