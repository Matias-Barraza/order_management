
package com.matiasbarraza.ordermanagementapi;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void usersEndpointShouldReturn401WithoutToken() throws Exception {

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void usersEndpointShouldReturn401WithMalformedToken() throws Exception {

        mockMvc.perform(
                get("/v1/users")
                        .header(
                                "Authorization",
                                "Bearer this-is-not-a-valid-jwt"
                        )
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void usersEndpointShouldReturn403ForUserRole() throws Exception {

        mockMvc.perform(
                get("/v1/users")
                        .with(user("test-user").roles("USER"))
        ).andExpect(status().isForbidden());
    }


    @Test
    void usersEndpointShouldAllowAdminRole() throws Exception {

        mockMvc.perform(
                get("/v1/users")
                        .with(user("test-admin").roles("ADMIN"))
        ).andExpect(status().isOk());
    }


}
