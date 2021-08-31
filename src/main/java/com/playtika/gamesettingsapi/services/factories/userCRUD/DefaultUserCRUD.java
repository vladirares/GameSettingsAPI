package com.playtika.gamesettingsapi.services.factories.userCRUD;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import com.playtika.gamesettingsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultUserCRUD implements UserCRUD {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<User> getAllUsers(Pageable pageable){
        List<User> users = new ArrayList<>();
        users.addAll(userRepository.findUsersByManager(pageable));

        return users;
    }

    @Override
    public User createUser(UserCRUDDTO userDTO) {
        User userToCreate = new User();
        updateUserWithDTO(userToCreate,userDTO);
        return userService.createUser(userToCreate);
    }

    @Override
    public User updateUser(UserCRUDDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).get();
        if(userRepository.existsByUsername(userDTO.getUsername()) && !user.getUsername().equals(userDTO.getUsername())){
            throw new IllegalArgumentException();
        }
        updateUserWithDTO(user,userDTO);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public boolean deleteUser(long id) {
        if(userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            userRepository.delete(user);
            return true;
        }
        return false;
    }


    public boolean hasIllegalRole(UserCRUDDTO userDTO){
        List<RoleType> roles = new ArrayList<>();
        roles.addAll(userDTO.getRoles().stream().map(Role::getName).map(RoleType::valueOf).collect(Collectors.toList()));
        return roles.contains(RoleType.ROLE_ADMIN);
    }

    public void updateUserWithDTO(User user,UserCRUDDTO userDTO){

        if(userDTO.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if(userDTO.getUsername()!=null){
            user.setUsername(userDTO.getUsername());
        }
        if(userDTO.getFirstName()!=null){
            user.setFirstName(userDTO.getFirstName());
        }
        if(userDTO.getLastName()!=null){
            user.setLastName(userDTO.getLastName());
        }
        if(userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }
        user.setMaxPlaytime(userDTO.getMaxPlaytime());
        if(userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()){
            List<Role> userRoles = new ArrayList<>();
            for(Role role : userDTO.getRoles()){
                userRoles.add(roleRepository.findByName(role.getName()));
            }
            user.setRoles(userRoles);
        }

    }

}
