package com.playtika.gamesettingsapi.services.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.DefaultGameSessionCRUD;
import com.playtika.gamesettingsapi.services.factories.gamesessionCRUD.GameSessionCRUD;
import com.playtika.gamesettingsapi.services.factories.userCRUD.DefaultUserCRUD;
import com.playtika.gamesettingsapi.services.factories.userCRUD.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AdminService implements UserCRUD, GameSessionCRUD {

    @Autowired
    GameSessionRepository gameSessionRepository;
    @Autowired
    DefaultUserCRUD defaultUserCRUD;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DefaultGameSessionCRUD defaultGameSessionCRUD;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        if(gameSessionDTO.getUserName() == null)
        {
            gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        }
        return  defaultGameSessionCRUD.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        return defaultGameSessionCRUD.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return gameSessionRepository.findAll();
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        return defaultGameSessionCRUD.deleteGameSession(gameSessionDTO);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = defaultUserCRUD.getAllUsers();
        users.addAll(userRepository.findByRoles(RoleType.ROLE_ADMIN.name()));
        return users;
    }

    @Override
    public User createUser(UserCRUDDTO userDTO) {
        return defaultUserCRUD.createUser(userDTO);
    }

    @Override
    public User updateUser(UserCRUDDTO userDTO) {
        return defaultUserCRUD.updateUser(userDTO);
    }

    @Override
    public boolean deleteUser(long id) {
        return defaultUserCRUD.deleteUser(id);
    }

}
