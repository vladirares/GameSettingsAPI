package com.playtika.gamesettingsapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.config.Settings;
import com.playtika.gamesettingsapi.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping
    @RequestMapping("/{gameName}")
    public String getGame(@PathVariable String gameName) throws ExecutionException, InterruptedException, SSLException, JsonProcessingException {
        return gameService.findGame(gameName).get();
    }
}
