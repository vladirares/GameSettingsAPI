package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findGameByName(String name);
}
