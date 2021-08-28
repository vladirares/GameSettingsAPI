package com.playtika.gamesettingsapi.services.servicerolefactory;

import com.playtika.gamesettingsapi.security.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCRUDFactory {
    UserCRUD userCRUD;

    @Autowired
    AdminService adminService;
    @Autowired
    ManagerService managerService;

    public UserCRUD createService(List<Role> roles) {
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        if(roleNames.contains("ROLE_ADMIN")){
            return adminService;
        }
        if(roleNames.contains("ROLE_MANAGER")){
            return managerService;
        }
        return null;
    }

}
