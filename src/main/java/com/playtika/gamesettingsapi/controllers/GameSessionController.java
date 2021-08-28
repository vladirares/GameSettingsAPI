package com.playtika.gamesettingsapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.servicerolefactory.GameSessionCRUDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class GameSessionController {

    @Autowired
    GameSessionCRUDFactory gameSessionCRUDFactory;

    @Autowired
    UserService userService;

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<GameSession> createGameSession(@RequestBody GameSessionDTO gameSessionDTO, Authentication auth) {
        try {
            User user = userService.getUser(auth.getName());
            gameSessionDTO.setUser(user);
            GameSession gameSession = gameSessionCRUDFactory.createService(user.getRoles()).createGameSession(gameSessionDTO);
            return new ResponseEntity<>(gameSession, HttpStatus.OK);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/records")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GameSession>> readGameSessions(Authentication auth) {
        User user = userService.getUser(auth.getName());
        List<GameSession> gameSessions = gameSessionCRUDFactory.createService(user.getRoles()).getGameSessions(user);
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
        return new ResponseEntity<>(gameSessionCRUDFactory.createService(user.getRoles()).updateGameSession(gameSessionDTO), HttpStatus.OK);

    }

    @DeleteMapping(value = "/records/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<Boolean> deleteGameSession(@PathVariable long id, Authentication auth) throws RuntimeException {
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        User user = userService.getUser(auth.getName());
        gameSessionDTO.setUser(user);
        gameSessionDTO.setId(id);
        boolean response = gameSessionCRUDFactory.createService(user.getRoles()).deleteGameSession(gameSessionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
