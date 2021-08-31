package com.playtika.gamesettingsapi.unittests.servicesTests.gamesessioncrudtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.services.GameSessionService;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(MockitoJUnitRunner.class)
public class DefaultGameSessionCRUDTests {

    @BeforeClass
    public void init() {
        defaultUserCrud = new DefaultUserCRUD();
    }

    @Mock
    GameSessionService gameSessionService;
    @Mock
    DefaultGameSessionCRUD defaultGameSessionCRUD;
    @InjectMocks
    DefaultUserCRUD defaultUserCrud;
    @InjectMocks
    RegularUserService regularUserService;

    @Test
    public void updateGameSessionTestRegularUserWrongId() {
        when(gameSessionService.getGameSession(any(Integer.class))).thenReturn(null);
        User user = new User();
        user.setGameSessions(new ArrayList<>());
        user.setUsername("username");
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setId(1);
        gameSessionDTO.setUser(user);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            regularUserService.updateGameSession(gameSessionDTO);
        });
    }

    @Test
    public void updateGameSessionTestRegularUserValidId() throws ExecutionException, InterruptedException, JsonProcessingException {
        GameSession gameSession = new GameSession();
        gameSession.setId(1);
        when(gameSessionService.getGameSession(1)).thenReturn(gameSession);
        User user = new User();
        user.setGameSessions(new ArrayList<>());
        user.setUsername("username");
        user.getGameSessions().add(gameSession);
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setId(1);
        gameSessionDTO.setUser(user);
        when(defaultGameSessionCRUD.updateGameSession(gameSessionDTO)).thenReturn(gameSession);

        Assertions.assertEquals(gameSession.getId(), regularUserService.updateGameSession(gameSessionDTO).getId());
    }

}
