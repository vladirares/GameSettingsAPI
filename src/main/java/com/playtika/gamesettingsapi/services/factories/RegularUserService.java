package com.playtika.gamesettingsapi.services.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.DefaultGameSessionCRUD;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.GameSessionCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RegularUserService implements GameSessionCRUD {

    @Autowired
    GameSessionService gameSessionService;
    @Autowired
    DefaultGameSessionCRUD defaultGameSessionCRUD;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        return defaultGameSessionCRUD.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        GameSession gameSession = gameSessionService.getGameSession(gameSessionDTO.getId());
        if (gameSession == null) {
            throw new IllegalArgumentException();
        }
        gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        boolean idNotValid = !gameSessionDTO.getUser().getGameSessions().contains(gameSession);
        if (idNotValid) {
            throw new IllegalArgumentException();
        }
        return defaultGameSessionCRUD.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user, Pageable pageable) {
        return defaultGameSessionCRUD.getGameSessions(user, pageable);
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        GameSession gameSession = gameSessionService.getGameSession(gameSessionDTO.getId());
        boolean idNotValid = !gameSessionDTO.getUser().getGameSessions().contains(gameSession);
        if (idNotValid) {
            return false;
        }
        return defaultGameSessionCRUD.deleteGameSession(gameSessionDTO);
    }

}
