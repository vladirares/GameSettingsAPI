package com.playtika.gamesettingsapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.GameSessionId;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameRepository;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GameSessionService {

    @Autowired
    GameSessionRepository gameSessionRepository;
    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;

    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws JsonProcessingException, ExecutionException, InterruptedException {
        Game game = gameService.createGame(gameSessionDTO.getGameName());
        User user = userService.getUser(gameSessionDTO.getUserName());
        GameSession gameSession = new GameSession();
        gameSession.setGame(game);
        gameSession.setUser(user);
        gameSession.setDuration(gameSessionDTO.getDuration());
        gameSession.setStartTime(gameSessionDTO.getStartTime());
        return gameSessionRepository.saveAndFlush(gameSession);
    }

    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        GameSession gameSession = gameSessionRepository.getById(gameSessionDTO.getId());
        Game game = gameService.createGame(gameSessionDTO.getGameName());
        if(game!= null){
            gameSession.setGame(game);
        }else{
            throw new IllegalArgumentException();
        }
        gameSession.setDuration(gameSessionDTO.getDuration());
        gameSession.setStartTime(gameSessionDTO.getStartTime());
        return gameSessionRepository.saveAndFlush(gameSession);
    }

}
