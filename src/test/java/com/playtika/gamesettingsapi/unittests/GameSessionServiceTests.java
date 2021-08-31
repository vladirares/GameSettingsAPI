//package com.playtika.gamesettingsapi.unittests;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.playtika.gamesettingsapi.dto.GameSessionDTO;
//import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
//import com.playtika.gamesettingsapi.models.Game;
//import com.playtika.gamesettingsapi.models.GameSession;
//import com.playtika.gamesettingsapi.models.User;
//import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
//import com.playtika.gamesettingsapi.repositories.UserRepository;
//import com.playtika.gamesettingsapi.security.models.Role;
//import com.playtika.gamesettingsapi.security.services.JwtTokenService;
//import com.playtika.gamesettingsapi.services.GameService;
//import com.playtika.gamesettingsapi.services.GameSessionService;
//import com.playtika.gamesettingsapi.services.UserService;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.concurrent.ExecutionException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@RunWith(MockitoJUnitRunner.class)
//public class GameSessionServiceTests
//{
//
//    @BeforeClass
//    public void init() {
//        userService = new UserService();
//    }
//
//    @Mock
//    GameSessionRepository gameSessionRepository;
//    @Mock
//    GameService gameService;
//    @Mock
//    UserService userService;
//    @InjectMocks
//    GameSessionService gameSessionService;
//
//    @Test
//    public void createGameSessionTestIsTimeExceeded() throws ExecutionException, JsonProcessingException, InterruptedException {
//        GameSessionDTO gameSessionDTO = new GameSessionDTO();
//        gameSessionDTO.setDuration(10);
//        Date date = new Date();
//        User user = new User();
//        user.setMaxPlaytime(0);
//        gameSessionDTO.setDuration(5);
//        gameSessionDTO.setStartTime(date);
//        gameSessionDTO.setUser(user);
//        ArrayList<GameSession> gameSessions = new ArrayList<>();
//        GameSession gameSession = new GameSession();
//        gameSession.setDuration(100);
//        gameSessions.add(gameSession);
////        when(gameSessionService.hasSurpassedMaxTime(user,anyInt(),any())).thenReturn(false);
//        when(gameService.createOrGetExistingGame(any())).thenReturn(new Game());
//        when(userService.getUser(any())).thenReturn(user);
//        when(gameSessionRepository.findGameSessionsByDate(any(),any())).thenReturn(gameSessions);
//
//        Assertions.assertFalse(gameSessionService.createGameSession(gameSessionDTO).isTimeExceeded());
//    }
//
//
//
//}