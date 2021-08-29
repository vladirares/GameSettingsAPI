package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.GameSessionId;
import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    @Query(value = "\tSELECT *\n" +
            "\tFROM game_sessions\n" +
            "INNER JOIN users\n" +
            "\tON users.id = game_sessions.user_id\n" +
            "\tAND users.username = :userName" +
            "\tAND game_sessions.game_session_start_time > ADDDATE(CAST(NOW() AS DATE),-1) ;",nativeQuery = true)
    List<GameSession> findByLastDay(String userName);
}
