package com.playtika.gamesettingsapi.services.factories;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.factories.userCRUD.DefaultUserCRUD;
import com.playtika.gamesettingsapi.services.factories.userCRUD.UserCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService extends RegularUserService implements UserCRUD {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DefaultUserCRUD defaultUserCRUD;

    @Override
    public List<User> getAllUsers(Pageable pageable){
        return defaultUserCRUD.getAllUsers(pageable);
    }

    @Override
    public User createUser(UserCRUDDTO userDTO) {
        if(hasIllegalRole(userDTO)){
            throw new IllegalArgumentException();
        }
        return defaultUserCRUD.createUser(userDTO);
    }

    @Override
    public User updateUser(UserCRUDDTO userDTO) {
        if(hasIllegalRole(userDTO)){
            throw new IllegalArgumentException();
        }
       return defaultUserCRUD.updateUser(userDTO);
    }

    @Override
    public boolean deleteUser(long id) {
        if(userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            if(user.getRoles().stream().map(Role::getName).anyMatch(x->x.equals("ROLE_ADMIN"))){
                throw new IllegalArgumentException();
            }
            return defaultUserCRUD.deleteUser(id);
        }
        return false;
    }

    protected boolean hasIllegalRole(UserCRUDDTO userDTO){
        List<RoleType> roles = new ArrayList<>();
        roles.addAll(userDTO.getRoles().stream().map(Role::getName).map(RoleType::valueOf).collect(Collectors.toList()));
        return roles.contains(RoleType.ROLE_ADMIN);
    }

}
