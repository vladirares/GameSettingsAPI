package com.playtika.gamesettingsapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
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
        Game game = gameService.createOrGetExistingGame(gameSessionDTO.getGameName());
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
        Game game = gameService.createOrGetExistingGame(gameSessionDTO.getGameName());
        if (game != null) {
            gameSession.setGame(game);
        } else {
            throw new IllegalArgumentException();
        }
        gameSession.setUser(userService.getUser(gameSessionDTO.getUserName()));
        gameSession.setDuration(gameSessionDTO.getDuration());
        gameSession.setStartTime(gameSessionDTO.getStartTime());
        return gameSessionRepository.saveAndFlush(gameSession);
    }

    public GameSession getGameSession(long id) {
        return gameSessionRepository.findById(id).get();
    }

    public boolean deleteGameSession(long id) {
        if(gameSessionRepository.findById(id).isPresent()){
            GameSession gameSession = gameSessionRepository.findById(id).get();
            gameSession.getUser().getGameSessions().remove(gameSession);
            gameSession.getGame().getGameSessions().remove(gameSession);
            gameSessionRepository.deleteById(id);
//            gameSessionRepository.flush();
            return true;
        }
        return false;
    }

}
