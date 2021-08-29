package com.playtika.gamesettingsapi.services.factories.userCRUD;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;

import java.util.List;

public interface UserCRUD {
    List<User> getAllUsers();
    User createUser(UserCRUDDTO userDTO);
    User updateUser(UserCRUDDTO userDTO);
    boolean deleteUser(long id);
}
