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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class GameService {

    private final static String URI = "https://api.rawg.io/api";
    private static SslContext sslContext;
    HttpClient httpClient;
    WebClient webClient;
    private static String API_KEY;

    @Autowired
    GameRepository gameRepository;

    @PostConstruct
    public void init() throws SSLException, FileNotFoundException {
        sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        webClient = WebClient.builder()
                .baseUrl(URI)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
        API_KEY = getApiSecret();
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

    public Game createOrGetExistingGame(String name) throws JsonProcessingException, ExecutionException, InterruptedException {
        Game gamesWithName = gameRepository.findGameByName(name);
        if(gamesWithName != null){
            return gamesWithName;
        }
        if(findGame(name).get()){
            return gameRepository.saveAndFlush(new Game(name));
        }
        return null;
    }

    public boolean updateGame(Game game) {
        Optional<Game> existingGame = gameRepository.findById(game.getId());
        if (existingGame.isPresent()) {
            gameRepository.saveAndFlush(game);
            return true;
        }
        return false;
    }

    @Async
    public Future<Boolean> findGame(String gameName) throws JsonProcessingException{

        String params = "/games?key=" +
                API_KEY +
                "&page=1&page_size=1&search=" +
                gameName +
                "&search_exact=true";

        String response = webClient.get()
                .uri(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        int count = getGameCount(response);
        boolean result = count > 0;

        return new AsyncResult<>(result);

    }

    private int getGameCount(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);
        return  jsonNode.get("count").asInt();
    }

    private String getApiSecret() throws FileNotFoundException {
        File file = new File("user.secrets/game.api.txt");
        Scanner scanner = new Scanner(file);
        String key = scanner.nextLine();
        scanner.close();
        return key;
    }

}