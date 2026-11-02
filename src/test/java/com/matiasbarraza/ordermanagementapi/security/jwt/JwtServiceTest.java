package com.matiasbarraza.ordermanagementapi.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET_KEY = "order-management-api-test-secret-key-at-least-32-bytes";
    private static final long TOKEN_EXPIRATION = 60_000L;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);

        ReflectionTestUtils.setField(jwtService, "jwtExpiration", TOKEN_EXPIRATION);
    }

    private UserDetails createUser(String email) {
        return User.withUsername(email)
                .password("test-password")
                .roles("USER")
                .build();
    }

    @Test
    void generateTokenShouldExtractUsernameAndValidateToken() {

        // Arrange
        UserDetails user = createUser("user@test.com");

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isBlank());

        assertEquals(
                "user@test.com",
                jwtService.extractUsername(token)
        );

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValidShouldReturnFalseForDifferentUsername() {

        // Arrange
        UserDetails originalUser = createUser("user@test.com");
        UserDetails differentUser = createUser("other@test.com");

        String token = jwtService.generateToken(originalUser);

        // Act & Assert
        assertFalse(
                jwtService.isTokenValid(token, differentUser)
        );
    }

    @Test
    void isTokenValidShouldReturnFalseWhenUserIsDisabled() {

        // Arrange
        String email = "user@test.com";

        UserDetails enabledUser = createUser(email);

        UserDetails disabledUser = User.withUsername(email)
                .password("test-password")
                .roles("USER")
                .disabled(true)
                .build();

        String token = jwtService.generateToken(enabledUser);

        // Act & Assert
        assertFalse(
                jwtService.isTokenValid(token, disabledUser)
        );
    }

    @Test
    void isTokenValidShouldReturnFalseForMalformedToken() {

        // Arrange
        UserDetails user = createUser("user@test.com");

        // Act & Assert
        assertFalse(
                jwtService.isTokenValid(
                        "this-is-not-a-valid-jwt",
                        user
                )
        );
    }

    @Test
    void isTokenValidShouldReturnFalseForExpiredToken() {

        // Arrange
        UserDetails user = createUser("user@test.com");

        // Generate a token that expired one second ago.
        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                -1_000L
        );

        String expiredToken = jwtService.generateToken(user);

        // Act & Assert
        assertFalse(
                jwtService.isTokenValid(expiredToken, user)
        );
    }

    @Test
    void getExpirationTimeShouldReturnConfiguredValue() {

        // Act
        long expiration = jwtService.getExpirationTime();

        // Assert
        assertEquals(TOKEN_EXPIRATION, expiration);
    }
}