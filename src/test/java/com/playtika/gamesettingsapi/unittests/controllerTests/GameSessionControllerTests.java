package com.playtika.gamesettingsapi.unittests.controllerTests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.factories.ManagerService;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.GameSessionCRUDFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GameSessionControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private GameSessionCRUDFactory gameSessionCRUDFactory;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private ManagerService managerService;

    @BeforeEach
    private void init() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testCreateGameSessionNotTimeExeeded() throws Exception {

        User user = new User();
        user.setUsername("test");
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        GameSession gameSession = new GameSession();
        gameSession.setTimeExceeded(false);

        String fullURL = "/api/create";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(gameSessionCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.createGameSession(any()))
                .thenReturn(gameSession);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isTimeExceeded").value("false"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testCreateGameSessionTimeExeeded() throws Exception {

        User user = new User();
        user.setUsername("test");
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        GameSession gameSession = new GameSession();
        gameSession.setTimeExceeded(true);

        String fullURL = "/api/create";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(gameSessionCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.createGameSession(any()))
                .thenReturn(gameSession);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSessionDTO)))
                .andExpect(status().isIAmATeapot())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isTimeExceeded").value("true"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testUpdateGameSessions() throws Exception {

        User user = new User();
        user.setUsername("test");
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setDuration(100);
        GameSession gameSession = new GameSession();
        gameSession.setDuration(100);

        String fullURL = "/api/records/1";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(gameSessionCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.updateGameSession(any()))
                .thenReturn(gameSession);
        mockMvc.perform(put(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(100));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testDeleteGameSessions() throws Exception {

        User user = new User();
        user.setUsername("test");
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setDuration(100);
        GameSession gameSession = new GameSession();
        gameSession.setDuration(100);

        String fullURL = "/api/records/delete/1";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(gameSessionCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.deleteGameSession(any()))
                .thenReturn(true);
        mockMvc.perform(delete(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

}