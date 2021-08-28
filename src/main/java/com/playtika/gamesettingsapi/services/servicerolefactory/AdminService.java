package com.playtika.gamesettingsapi.services.servicerolefactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtika.gamesettingsapi.dto.GameSessionDTO;
import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.GameSession;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.GameSessionRepository;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AdminService extends ManagerService{

    @Autowired
    ManagerService managerService;

    @Autowired
    GameSessionRepository gameSessionRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public GameSession createGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        if(gameSessionDTO.getUserName() == null)
        {
            gameSessionDTO.setUserName(gameSessionDTO.getUser().getUsername());
        }
        return  gameSessionService.createGameSession(gameSessionDTO);
    }

    @Override
    public GameSession updateGameSession(GameSessionDTO gameSessionDTO) throws InterruptedException, ExecutionException, JsonProcessingException {
        return gameSessionService.updateGameSession(gameSessionDTO);
    }

    @Override
    public List<GameSession> getGameSessions(User user) {
        return gameSessionRepository.findAll();
    }

    @Override
    public boolean deleteGameSession(GameSessionDTO gameSessionDTO) {
        return gameSessionService.deleteGameSession(gameSessionDTO.getId());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(userRepository.findByRoles(RoleType.ROLE_USER.name()));
        users.addAll(userRepository.findByRoles(RoleType.ROLE_MANAGER.name()));
        users.addAll(userRepository.findByRoles(RoleType.ROLE_ADMIN.name()));
        return users;
    }

    @Override
    public User createUser(UserCRUDDTO userDTO) {
        User userToCreate = new User();
        userToCreate.setEmail(userDTO.getEmail());
        userToCreate.setUsername(userDTO.getUsername());
        userToCreate.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> userRoles = new ArrayList<>();
        for(Role role : userDTO.getRoles()){
            userRoles.add(roleRepository.findByName(role.getName()));
        }
        userToCreate.setRoles(userRoles);
        return userService.createUser(userToCreate);
    }

}
