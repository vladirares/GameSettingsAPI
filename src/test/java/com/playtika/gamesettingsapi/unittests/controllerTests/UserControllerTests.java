package com.playtika.gamesettingsapi.unittests.controllerTests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.security.dto.LoginRequest;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.factories.ManagerService;
import com.playtika.gamesettingsapi.services.factories.userCRUD.UserCRUDFactory;
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
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserCRUDFactory userCRUDFactory;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private ManagerService managerService;

    @BeforeEach
    private void init() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void testLoginStatusOk() throws Exception {
        String username = "username";
        LoginResponse loginResponse;
        LoginRequest loginRequest;
        loginResponse = new LoginResponse();
        loginResponse.setUserName(username);
        loginResponse.setEmail("email");
        loginResponse.setAccessToken("token");
        loginRequest = new LoginRequest();
        loginRequest.setUserName(username);
        loginRequest.setPassword("password");

        String fullURL = "/api/login";
        when(userService.login(any(String.class), any(String.class)))
                .thenReturn(loginResponse);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value(loginResponse.getUserName()));
    }

    @Test
    void testRegisterStatusOk() throws Exception {
        String username = "username";
        SignUpRequest signUpRequest;
        signUpRequest = new SignUpRequest();
        signUpRequest.setUserName(username);
        signUpRequest.setPassword("1234");
        signUpRequest.setEmail("email");
        User user = new User();
        user.setUsername("username");

        String fullURL = "/api/register";
        when(userService.signUp(any()))
                .thenReturn(user);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testCreateUser() throws Exception {

        User user = new User();
        user.setUsername("test");
        UserCRUDDTO userDTO = new UserCRUDDTO();
        userDTO.setUsername("test");

        String fullURL = "/api/users/create";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(userCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.createUser(any()))
                .thenReturn(user);
        mockMvc.perform(post(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"}, username = "user")
    void testUpdateUser() throws Exception {

        User user = new User();
        user.setUsername("test");
        UserCRUDDTO userDTO = new UserCRUDDTO();
        userDTO.setUsername("test");

        String fullURL = "/api/users/update";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(userCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.updateUser(any()))
                .thenReturn(user);
        mockMvc.perform(put(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testDeleteUser() throws Exception {

        User user = new User();
        user.setUsername("test");
        UserCRUDDTO userDTO = new UserCRUDDTO();
        userDTO.setUsername("test");
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_USER");
        userDTO.setRoles(roles);

        String fullURL = "/api/users/delete/1";
        when(userService.getUser(any()))
                .thenReturn(user);
        when(userCRUDFactory.createService(any()))
                .thenReturn(managerService);
        when(managerService.deleteUser(anyLong()))
                .thenReturn(true);
        mockMvc.perform(delete(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}, username = "user")
    void testSetMaxDailyPlayTime() throws Exception {

        User user = new User();
        user.setMaxPlaytime(100);
        user.setUsername("test");
        UserCRUDDTO userDTO = new UserCRUDDTO();
        userDTO.setUsername("test");
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_USER");
        userDTO.setRoles(roles);

        String fullURL = "/api/maxdailyplaytime?time=100";
        when(userService.setMaxPlayTime(any()))
                .thenReturn(user);
        mockMvc.perform(put(fullURL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxPlaytime").value(100));
    }


}