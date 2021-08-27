package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findGameByName(String name);
}
