package com.playtika.gamesettingsapi.unittests.servicesTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import com.playtika.gamesettingsapi.services.GameService;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.UserService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class GameSessionServiceTests
{

    @BeforeClass
    public void init() {
        userService = new UserService();
    }

    @Mock
    GameSessionRepository gameSessionRepository;
    @Mock
    GameService gameService;
    @Mock
    UserService userService;
    @InjectMocks
    GameSessionService gameSessionService;

    @Test
    public void createGameSessionTestIsTimeExceededTest() throws ExecutionException, JsonProcessingException, InterruptedException {
        User user = new User();
        GameSession gameSession = new GameSession();
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setStartTime(new Date());
        gameSessionDTO.setDuration(100);
        when(gameService.createOrGetExistingGame(any())).thenReturn(new Game());
        when(userService.getUser(any())).thenReturn(user);
        when(gameSessionRepository.saveAndFlush(any())).thenReturn(gameSession);

        Assertions.assertFalse(gameSessionService.createGameSession(gameSessionDTO).isTimeExceeded());
    }

    @Test
    public void updateGameSessionTest() throws ExecutionException, JsonProcessingException, InterruptedException {
        User user = new User();
        GameSession gameSession = new GameSession();
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setStartTime(new Date());
        gameSessionDTO.setDuration(101);
        when(gameService.createOrGetExistingGame(any())).thenReturn(new Game());
        when(userService.getUser(any())).thenReturn(user);
        when(gameSessionRepository.saveAndFlush(any())).thenReturn(gameSession);
        when(gameSessionRepository.getById(any())).thenReturn(gameSession);

        Assertions.assertEquals(101,gameSessionService.updateGameSession(gameSessionDTO).getDuration());
    }

    @Test
    public void deleteGameSessionTest(){
        User user = new User();
        ArrayList<GameSession> gameSessions = new ArrayList<>();
        GameSession gameSession = new GameSession();
        gameSessions.add(gameSession);
        user.setGameSessions(gameSessions);
        gameSession.setUser(user);
        Game game = new Game();
        game.setGameSessions(gameSessions);
        gameSession.setGame(game);
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setStartTime(new Date());
        gameSessionDTO.setDuration(101);
        when(gameSessionRepository.findById(any())).thenReturn(Optional.of(gameSession));
        when(gameSessionRepository.findById(any())).thenReturn(Optional.of(gameSession));

        Assertions.assertEquals(true,gameSessionService.deleteGameSession(1));
    }

}