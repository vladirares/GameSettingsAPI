package com.playtika.gamesettingsapi.unittests.controllerTests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.security.dto.LoginRequest;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.UserService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    private LoginResponse loginResponse;
    private LoginRequest loginRequest;
    private SignUpRequest signUpRequest;

    @BeforeEach
    private void init() {
        String username = "username";
        signUpRequest = new SignUpRequest();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        loginResponse = new LoginResponse();
        loginResponse.setUserName(username);
        loginResponse.setEmail("email");
        loginResponse.setAccessToken("token");
        loginRequest = new LoginRequest();
        loginRequest.setUserName(username);
        loginRequest.setPassword("password");
        signUpRequest.setUserName(username);
        signUpRequest.setPassword("1234");
        signUpRequest.setEmail("email");

    }
    @Test
    void testLoginStatusOk() throws Exception {
        String fullURL = "/api/login";
        when(userService.login(any(String.class), any(String.class)))
                .thenReturn(loginResponse);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(loginResponse.getUserName()));
    }

    @Test
    void testRegisterStatusOk() throws Exception {
        User user = new User();
        user.setUsername("username");
        String fullURL = "/api/register";
        when(userService.signUp(any()))
                .thenReturn(user);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));
    }



}