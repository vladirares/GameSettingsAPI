package com.playtika.gamesettingsapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.repositories.GameRepository;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.io.*;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class GameService {

    private final static String URI = "https://api.rawg.io/api";
    private static SslContext sslContext;
    HttpClient httpClient;
    WebClient webClient;

    @Autowired
    GameRepository gameRepository;

    @PostConstruct
    public void init() throws SSLException {
        sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        webClient = WebClient.builder()
                .baseUrl(URI)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findById(long id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElse(null);
    }

    public boolean deleteById(long id) {
        if (gameRepository.findById(id).isPresent()) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Game createGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }

    public boolean updateUser(Game game) {
        Optional<Game> existingGame = gameRepository.findById(game.getId());
        if (existingGame.isPresent()) {
            gameRepository.saveAndFlush(game);
            return true;
        }
        return false;
    }

    @Async
    public Future<String> findGame(String gameName) throws SSLException, JsonProcessingException {

        String params = "/games?key=c6a7575e5863480e9611482b5f7e8c48&page=1&page_size=1&search=" + gameName + "&search_exact=true";

        String response = webClient.get()
                .uri(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        int count = jsonNode.get("count").asInt();
        Boolean result = count > 0;

        return new AsyncResult<String>(result.toString());

    }

    private String getApiSecret() throws FileNotFoundException {
        File file = new File("user.secrets/game.api.txt");
        Scanner scanner = new Scanner(file);
        String key = scanner.nextLine();
        scanner.close();
        return key;
    }

}