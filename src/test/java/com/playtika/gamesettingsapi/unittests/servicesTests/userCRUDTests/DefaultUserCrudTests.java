package com.playtika.gamesettingsapi.unittests.servicesTests.userCRUDTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.factories.RegularUserService;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.DefaultGameSessionCRUD;
import com.playtika.gamesettingsapi.services.factories.userCRUD.DefaultUserCRUD;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class DefaultUserCrudTests {

    @BeforeClass
    public void init() {
        defaultUserCrud = new DefaultUserCRUD();
    }

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;
    @Mock
    GameSessionService gameSessionService;
    @Mock
    DefaultGameSessionCRUD defaultGameSessionCRUD;
    @InjectMocks
    DefaultUserCRUD defaultUserCrud;
    @InjectMocks
    RegularUserService regularUserService;

    @Test
    public void hasIllegalRoleTest() {
        UserCRUDDTO userDTO = new UserCRUDDTO();
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roles.add(role);
        userDTO.setRoles(roles);
        Assertions.assertEquals(true, defaultUserCrud.hasIllegalRole(userDTO));
    }


}