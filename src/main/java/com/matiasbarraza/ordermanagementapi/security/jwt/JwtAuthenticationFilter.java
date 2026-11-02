package com.matiasbarraza.ordermanagementapi.security.jwt;

import com.matiasbarraza.ordermanagementapi.security.RestSecurityErrorHandler;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RestSecurityErrorHandler securityErrorHandler;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7).trim();

            if (jwt.isBlank()) {
                throw new IllegalArgumentException("Bearer token is empty");
            }

            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail == null || userEmail.isBlank()) {
                throw new IllegalArgumentException("Token subject is missing");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(userEmail);

                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    rejectToken(request, response);
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (JwtException
                 | IllegalArgumentException
                 | UsernameNotFoundException exception) {

            rejectToken(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void rejectToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {

        SecurityContextHolder.clearContext();

        securityErrorHandler.commence(
                request,
                response,
                new BadCredentialsException("Invalid or expired bearer token")
        );
    }
}