package com.playtika.gamesettingsapi.services.factories.gamesessionCRUD;

import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.services.factories.AdminService;
import com.playtika.gamesettingsapi.services.factories.ManagerService;
import com.playtika.gamesettingsapi.services.factories.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameSessionCRUDFactory {
    GameSessionCRUD gameSessionCRUD;

    @Autowired
    AdminService adminService;
    @Autowired
    ManagerService managerService;
    @Autowired
    RegularUserService regularUserService;

    public GameSessionCRUD createService(List<Role> roles) {
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        if(roleNames.contains("ROLE_ADMIN")){
            return adminService;
        }
        if(roleNames.contains("ROLE_MANAGER")){
            return managerService;
        }
        if(roleNames.contains("ROLE_USER")){
            return regularUserService;
        }
        return null;
    }

}
