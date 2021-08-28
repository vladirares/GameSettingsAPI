package com.playtika.gamesettingsapi.services.servicerolefactory;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.exceptions.MyCustomException;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import com.playtika.gamesettingsapi.services.GameSessionService;
import com.playtika.gamesettingsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManagerService extends RegularUserService implements UserCRUD{

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        users.addAll(userRepository.findByRoles(RoleType.ROLE_USER.name()));
        users.addAll(userRepository.findByRoles(RoleType.ROLE_MANAGER.name()));

        return users;
    }

    @Override
    public User createUser(UserCRUDDTO userDTO) {
        List<RoleType> roles = new ArrayList<>();
        roles.addAll(userDTO.getRoles().stream().map(Role::getName).map(RoleType::valueOf).collect(Collectors.toList()));
        if(roles.contains(RoleType.ROLE_ADMIN)){
            throw new IllegalArgumentException();
        }
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
