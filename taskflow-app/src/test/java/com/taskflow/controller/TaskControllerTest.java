package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
 
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test slice pour `TaskController`.
 *
 * - Utilise `@WebMvcTest` pour charger uniquement le controller et ses dépendances web.
 * - `TaskService` est mocké via `@MockBean`.
 * - Adapte les assertions selon la structure réelle de la réponse JSON de ton controller.
 */
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService; // sera injecté dans le controller mocké

    @Test
    @WithMockUser
    void postCreateTask_returnsCreated() throws Exception {
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("Controller task");
        req.setDescription("desc");

        // Par défaut on ne stub pas taskService.createTask ici ; le controller
        // doit être capable de renvoyer la réponse (ou tu peux stubber si besoin).
        Mockito.when(taskService.createTask(Mockito.any(), Mockito.any())).thenReturn(null);

        mockMvc.perform(post("/api/tasks")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());
    }
}
