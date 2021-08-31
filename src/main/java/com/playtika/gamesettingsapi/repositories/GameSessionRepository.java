package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    @Query(value = "\tSELECT *\n" +
            "\tFROM game_sessions\n" +
            "INNER JOIN users\n" +
            "\tON users.id = game_sessions.user_id\n" +
            "\tAND users.username = :userName" +
            "\tAND DATE(game_sessions.game_session_start_time) BETWEEN" +
            " DATE(:gameSessionStartDate) AND" +
            " ADDDATE(DATE(:gameSessionStartDate),+1);",nativeQuery = true)
    List<GameSession> findGameSessionsByDate(String userName, Date gameSessionStartDate);

    @Query(value = "SELECT *\n" +
            "FROM game_sessions\n" +
            "INNER JOIN users\n" +
            "ON users.id = game_sessions.user_id\n" +
            "AND users.username = :userName " ,nativeQuery = true)
    List<GameSession> findGameSessionsByUserName(String userName, Pageable pageable);

    @Query(value = "SELECT * FROM game_sessions",nativeQuery = true)
    List<GameSession> findAllGameSessions(Pageable pageable);

}
