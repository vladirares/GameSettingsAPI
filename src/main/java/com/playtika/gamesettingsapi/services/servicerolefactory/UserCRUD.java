package com.playtika.gamesettingsapi.services.servicerolefactory;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;

import java.util.List;

public interface UserCRUD {
    List<User> getAllUsers();
    User createUser(UserCRUDDTO userDTO);
    User updateUser(UserCRUDDTO userDTO);
    boolean deleteUser(long id);
}
