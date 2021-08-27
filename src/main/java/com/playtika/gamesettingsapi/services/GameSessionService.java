package com.playtika.gamesettingsapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
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

    public GameSession createGameSession(GameSession gameSession) throws JsonProcessingException, ExecutionException, InterruptedException {
        Game game = gameService.createGame(gameSession.getGame());
        gameSession.setGame(game);
        return gameSessionRepository.saveAndFlush(gameSession);
    }

}
