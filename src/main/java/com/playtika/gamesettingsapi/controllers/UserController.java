package com.playtika.gamesettingsapi.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.security.dto.LoginRequest;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.services.GameService;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.servicerolefactory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GameSessionService gameSessionService;

    @Autowired
    GameService gameService;

    @Autowired
    ServiceFactory serviceFactory;


    @GetMapping
    @RequestMapping("/login")
    public String login() {
        return "Login page";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest requestHeader, @RequestBody LoginRequest request) throws RuntimeException {

        LoginResponse loginResponse = userService.login(request.getUserName(), request.getPassword());
        if (loginResponse == null) {
            throw new RuntimeException("Login failed. Possible cause : incorrect username/password");
        } else {
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<User> signUp(HttpServletRequest requestHeader, @RequestBody SignUpRequest request) throws RuntimeException {

        User user;
        try {
            user = userService.signUp(request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<GameSession> createGameSession(@RequestBody GameSessionDTO gameSessionDTO, Authentication auth) {
        try {
            User user = userService.getUser(auth.getName());
            gameSessionDTO.setUser(user);
            GameSession gameSession = serviceFactory.createService(user.getRoles()).createGameSession(gameSessionDTO);
            return new ResponseEntity<>(gameSession, HttpStatus.OK);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/records")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GameSession>> readGameSessions(Authentication auth) {
        User user = userService.getUser(auth.getName());
        List<GameSession> gameSessions = serviceFactory.createService(user.getRoles()).getGameSessions(user);
        if (gameSessions != null) {
            return new ResponseEntity<>(gameSessions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/records/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<GameSession> updateGameSession(@PathVariable long id, @RequestBody GameSessionDTO gameSessionDTO, Authentication auth) throws InterruptedException, ExecutionException, JsonProcessingException {
        User user = userService.getUser(auth.getName());
        gameSessionDTO.setUser(user);
        gameSessionDTO.setId(id);
        return new ResponseEntity<>(serviceFactory.createService(user.getRoles()).updateGameSession(gameSessionDTO), HttpStatus.OK);

    }

    @DeleteMapping(value = "/records/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<Boolean> deleteGameSession(@PathVariable long id, Authentication auth) throws RuntimeException {
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        User user = userService.getUser(auth.getName());
        gameSessionDTO.setUser(user);
        gameSessionDTO.setId(id);
        boolean response = serviceFactory.createService(user.getRoles()).deleteGameSession(gameSessionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestParam String userName) throws RuntimeException {
        try {
            userService.removeUser(userName);
        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUser() throws RuntimeException {
        try {
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }

    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<UserDTO> searchUser(@RequestParam String userName) throws RuntimeException {

        UserDTO userResponse = userService.searchUser(userName);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refreshToken(HttpServletRequest req) {
        return userService.refreshToken(req.getRemoteUser());
    }

//    @ExceptionHandler
//    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
//    public ResponseEntity<String> handleException(Exception e) {
//        return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
