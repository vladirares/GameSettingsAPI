package com.playtika.gamesettingsapi.unittests.servicesTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.repositories.GameRepository;
import com.playtika.gamesettingsapi.services.GameService;
import com.playtika.gamesettingsapi.services.UserService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class GameServiceTests {

    @Mock
    GameRepository gameRepository;
    @Mock
    UserService userService;
    @Spy
    @InjectMocks
    GameService gameService;

    @Test
    public void findByIdTest() {
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

}