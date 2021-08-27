package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.Game;
import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

}
