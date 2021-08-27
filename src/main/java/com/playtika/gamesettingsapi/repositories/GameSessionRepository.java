package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.GameSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, GameSessionId> {
}
