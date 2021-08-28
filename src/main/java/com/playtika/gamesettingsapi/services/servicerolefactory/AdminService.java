package com.playtika.gamesettingsapi.services.servicerolefactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AdminService extends ManagerService{

    @Autowired
    ManagerService managerService;

    @Autowired
    GameSessionRepository gameSessionRepository;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        if(gameSessionDTO.getUserName() == null)
        {
            gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        }
        return  gameSessionService.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        return gameSessionService.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return gameSessionRepository.findAll();
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        return gameSessionService.deleteGameSession(gameSessionDTO.getId());
    }
}
