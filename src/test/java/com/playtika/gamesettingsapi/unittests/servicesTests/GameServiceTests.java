package com.playtika.gamesettingsapi.unittests.servicesTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameRepository;
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
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class GameServiceTests {

    @BeforeClass
    public void init() {
        userService = new UserService();
    }

    @Mock
    GameRepository gameRepository;
    @Mock
    UserService userService;
    @Mock
    WebClient webClient;
    @Spy
    @InjectMocks
    GameService gameService;

    @Test
    public void findByIdTest()  {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(new Game()));
        Assertions.assertNotNull(gameService.findById(1));
    }

    @Test
    public void createOrGetExistingGameInDBTest() throws JsonProcessingException, ExecutionException, InterruptedException {
        when(gameRepository.findGameByName(any())).thenReturn(new Game());
        doReturn(CompletableFuture.completedFuture(true)).when(gameService).findGame(any());
        Assertions.assertNotNull(gameService.createOrGetExistingGame("name"));
    }

    @Test
    public void createOrGetExistingGameInAPITest() throws JsonProcessingException, ExecutionException, InterruptedException {
        when(gameRepository.findGameByName(any())).thenReturn(null);
        when(gameRepository.saveAndFlush(any())).thenReturn(new Game());
        doReturn(CompletableFuture.completedFuture(true)).when(gameService).findGame(any());
        Assertions.assertNotNull(gameService.createOrGetExistingGame("name"));
    }

//    @Test
//    public void findGameTest() throws JsonProcessingException, ExecutionException, InterruptedException {
//        when(webClient.get()
//                .uri(any(String.class))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block()).thenReturn("{'count':'10'}");
//        when(gameRepository.saveAndFlush(any())).thenReturn(new Game());
//        doReturn(CompletableFuture.completedFuture(true)).when(gameService).findGame(any());
//        Assertions.assertNotNull(gameService.findGame("name"));
//    }

}