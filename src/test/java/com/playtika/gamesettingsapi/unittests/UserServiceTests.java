package com.playtika.gamesettingsapi.unittests;

import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.dto.LoginRequest;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.UserService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @BeforeClass
    public void init(){
        userService = new UserService();
    }

    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenService jwtTokenService;
    @InjectMocks
    UserService userService;

    @Test
    public void loginTestInvalidUsername() {
        String userName = "InvalidUsername";
        String password = "test123";
        when(userRepository.findByUsername(userName)).thenReturn(new User());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(AuthenticationException.class);
        when(jwtTokenService.createToken(userName,new ArrayList<Role>())).thenReturn("ok");

        Assertions.assertThrows(AuthenticationException.class,()->{userService.login(userName,password);});
    }

    @Test
    public void registerTestWithTakenUserName() {
        String userName = "InvalidUsername";
        String password = "test123";
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName(userName);
        signUpRequest.setPassword(password);
        when(userRepository.existsByUsername(userName)).thenReturn(true);

        Assertions.assertThrows(AuthenticationException.class,()->{userService.signUp(signUpRequest);});
    }

    @Test
    public void createUserTestFailTakenName() {
        String userName = "InvalidUsername";
        String password = "test123";
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        when(userRepository.existsByUsername(userName)).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,()->{userService.createUser(user);});
    }

    @Test
    public void createUserTestFailInvalidPassword() {
        String userName = "InvalidUsername";
        String password = "tes";
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        when(userRepository.existsByUsername(userName)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,()->{userService.createUser(user);});
    }

    @Test
    public void searchUserValid() {
        String userName = "ValidUsername";
        User user = new User();
        user.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(user);
        UserDTO userDTO = userService.searchUser(userName);
        Assertions.assertEquals(userDTO.getUserName(),user.getUsername());
    }

    @Test
    public void searchUserInValid() {
        String userName = "InvalidUsername";
        User user = new User();
        user.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(null);
        Assertions.assertThrows(AuthenticationException.class,()->{userService.searchUser(userName);});
    }

    @Test
    public void setMaxPlayTimeTest(){
        String userName = "InvalidUsername";
        User user = new User();
        user.setUsername(userName);
        when(userRepository.findByUsername(userName)).thenReturn(null);
        Assertions.assertThrows(AuthenticationException.class,()->{userService.searchUser(userName);});
    }


}
