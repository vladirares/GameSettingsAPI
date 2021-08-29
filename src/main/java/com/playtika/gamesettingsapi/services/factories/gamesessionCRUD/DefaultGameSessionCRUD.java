package com.playtika.gamesettingsapi.services.factories.gamesessionCRUD;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.services.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class DefaultGameSessionCRUD implements GameSessionCRUD{

    @Autowired
    GameSessionService gameSessionService;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        return  gameSessionService.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        return gameSessionService.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return user.getGameSessions();
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        return gameSessionService.deleteGameSession(gameSessionDTO.getId());
    }
}
