package com.playtika.gamesettingsapi.services.servicefactory;

import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements RoleService{

    @Autowired
    ManagerService managerService;

    @Autowired
    GameSessionRepository gameSessionRepository;

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) {
        return null;
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return gameSessionRepository.findAll();
    }
}
