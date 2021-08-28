package com.playtika.gamesettingsapi.services.servicerolefactory;

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
public class RegularUserService implements RoleService{

    @Autowired
    GameSessionService gameSessionService;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        return  gameSessionService.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        GameSession gameSession = gameSessionService.getGameSession(gameSessionDTO.getId());
        gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        boolean idNotValid = !gameSessionDTO.getUser().getGameSessions().contains(gameSession);
        if(idNotValid){
            throw new IllegalArgumentException();
        }
        return gameSessionService.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return user.getGameSessions();
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        GameSession gameSession = gameSessionService.getGameSession(gameSessionDTO.getId());
        boolean idNotValid = !gameSessionDTO.getUser().getGameSessions().contains(gameSession);
        if(idNotValid){
            return false;
        }
        return gameSessionService.deleteGameSession(gameSessionDTO.getId());
    }

}
