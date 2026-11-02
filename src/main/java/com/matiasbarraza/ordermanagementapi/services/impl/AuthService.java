package com.matiasbarraza.ordermanagementapi.services.impl;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.LoginRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.AuthResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.UserEntity;
import com.matiasbarraza.ordermanagementapi.repositories.RoleRepository;
import com.matiasbarraza.ordermanagementapi.repositories.UserEntityRepository;
import com.matiasbarraza.ordermanagementapi.security.UserEntityUserDetails;
import com.matiasbarraza.ordermanagementapi.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserEntity user = userEntityRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserEntityUserDetails userDetails = new UserEntityUserDetails(user);

        String jwtToken = jwtService.generateToken(userDetails);
        long expiresIn = jwtService.getExpirationTime();

        return new AuthResponse(user.getId(), user.getEmail(), jwtToken, expiresIn);
    }


}
