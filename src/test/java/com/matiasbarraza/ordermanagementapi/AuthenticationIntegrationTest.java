package com.matiasbarraza.ordermanagementapi;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginShouldReturnJwtAndTokenShouldAccessProtectedEndpoint() throws Exception {

        // 1. Send valid credentials to the login endpoint.
        MvcResult loginResult = mockMvc.perform(
                        post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "admin@email.com",
                                            "password": "demo1234"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@email.com"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        // 2. Extract the JWT from the response.
        String responseBody = loginResult.getResponse().getContentAsString();

        String token = JsonPath.read(responseBody, "$.token");

        assertNotNull(token);
        assertFalse(token.isBlank());

        // 3. Use the real JWT to access a protected endpoint.
        mockMvc.perform(
                        get("/v1/products")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldReturn401WithInvalidCredentials() throws Exception {

        mockMvc.perform(
                        post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "admin@email.com",
                                            "password": "incorrect123"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }
}
