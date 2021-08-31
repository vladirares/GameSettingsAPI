package com.playtika.gamesettingsapi.services.factories.userCRUD;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserCRUD {
    List<User> getAllUsers(Pageable pageable);
    User createUser(UserCRUDDTO userDTO);
    User updateUser(UserCRUDDTO userDTO);
    boolean deleteUser(long id);
}
