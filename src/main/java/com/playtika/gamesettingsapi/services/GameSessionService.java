package com.playtika.gamesettingsapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
        List<GameSession> divided = divideGameSession(gameSession);
        GameSession exceededTime = divided.get(0);
        for (int i = 0; i < divided.size(); i++) {
            divided.get(i).setTimeExceeded(hasSurpassedMaxTime(user, divided.get(i).getDuration(), divided.get(i).getStartTime()));
            if(divided.get(i).isTimeExceeded()){
                exceededTime = divided.get(i);
            }
            gameSessionRepository.saveAndFlush(divided.get(i));
        }

        return gameSessionRepository.saveAndFlush(exceededTime);
    }

    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        GameSession gameSession = gameSessionRepository.getById(gameSessionDTO.getId());
        Game game = gameService.createOrGetExistingGame(gameSessionDTO.getGameName());
        if (game != null) {
            gameSession.setGame(game);
        } else {
            throw new IllegalArgumentException();
        }
        if (gameSessionDTO.getUserName() != null) {
            gameSession.setUser(userService.getUser(gameSessionDTO.getUserName()));
        }
        if (gameSessionDTO.getStartTime() != null) {
            gameSession.setStartTime(gameSessionDTO.getStartTime());
        }
        gameSession.setDuration(gameSessionDTO.getDuration());
        return gameSessionRepository.saveAndFlush(gameSession);
    }

    public GameSession getGameSession(long id) {
        return gameSessionRepository.findById(id).get();
    }

    public boolean deleteGameSession(long id) {
        if (gameSessionRepository.findById(id).isPresent()) {
            GameSession gameSession = gameSessionRepository.findById(id).get();
            gameSession.getUser().getGameSessions().remove(gameSession);
            gameSession.getGame().getGameSessions().remove(gameSession);
            gameSessionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean hasSurpassedMaxTime(User user, int time, Date gameSessionStartDate) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (gameSessionStartDate == null) {
            throw new IllegalArgumentException();
        }
        if (user.getMaxPlaytime() == 0) {
            return false;
        }
        List<GameSession> gameSessions = gameSessionRepository.findGameSessionsByDate
                (user.getUsername(), gameSessionStartDate);
        if (gameSessions == null ) {
            return false;
        }
        int totalTime = gameSessions.stream()
                .map(GameSession::getDuration)
                .reduce(0, Integer::sum);
        totalTime += time;
        return totalTime > user.getMaxPlaytime();
    }

    private List<GameSession> divideGameSession(GameSession gameSession) {
        List<GameSession> gameSessions = new ArrayList<>();

        Date startTime = gameSession.getStartTime();
        Date endTime = addMinutesToDate(startTime, gameSession.getDuration());

        if(removeTime(endTime).getTime() - removeTime(startTime).getTime() > 0){
            while (removeTime(endTime).getTime() - removeTime(startTime).getTime() > 0) {
                GameSession newGameSession = new GameSession();
                newGameSession.setGame(gameSession.getGame());
                newGameSession.setUser(gameSession.getUser());
                newGameSession.setStartTime(startTime);
                newGameSession.setDuration(getDifferenceMinutes(removeTime(addDayToDate(startTime)), startTime));
                gameSessions.add(newGameSession);

                startTime = removeTime(addDayToDate(startTime));
                endTime = addMinutesToDate(startTime,
                        gameSession.getDuration() - gameSessions.stream().map(GameSession::getDuration)
                                .reduce(0, Integer::sum));
            }

            GameSession lastSession = new GameSession();
            lastSession.setGame(gameSession.getGame());
            lastSession.setUser(gameSession.getUser());
            lastSession.setStartTime(endTime);
            lastSession.setDuration(gameSession.getDuration() - gameSessions.stream().map(GameSession::getDuration)
                    .reduce(0, Integer::sum));
            gameSessions.add(lastSession);
        }else{
            gameSessions.add(gameSession);
        }

        return  gameSessions;

    }

    private Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private Date addDayToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }


    private int getDifferenceMinutes(Date d2, Date d1) {
        long diff = d2.getTime() - d1.getTime();
        return (int)TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }

}
