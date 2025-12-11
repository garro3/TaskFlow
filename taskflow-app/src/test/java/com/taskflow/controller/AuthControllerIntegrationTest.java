package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration minimal pour l'endpoint d'inscription `/api/auth/register`.
 *
 * - Démarre le contexte SpringBoot complet (controllers, services, repository)
 * - Utilise MockMvc pour effectuer une requête POST JSON
 * - `addFilters = false` désactive les filtres de sécurité pour ce test
 *   (si Spring Security est présent mais non configuré pour autoriser
 *    cet endpoint dans le projet de test)
 * - `@Transactional` s'assure que la base est rollbackée après le test
 *   pour ne pas laisser de données persistées entre tests.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // pour sérialiser le DTO en JSON

    @Test
    void registerEndpoint_createsUserAndReturnsUserResponse() throws Exception {
        // Prépare le DTO d'inscription
        UserRegisterRequest req = new UserRegisterRequest();
        req.setUsername("integration_user");
        req.setEmail("integration@example.com");
        req.setPassword("Password123!");

        // Effectue la requête POST /api/auth/register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("integration_user"))
                .andExpect(jsonPath("$.email").value("integration@example.com"))
                .andExpect(jsonPath("$.id").exists())
                // Assure que le champ password n'est pas exposé dans la réponse
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
