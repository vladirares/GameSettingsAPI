package com.playtika.gamesettingsapi.services.servicefactory;

import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceFactory {
    RoleService roleService;

    @Autowired
    AdminService adminService;
    @Autowired
    ManagerService managerService;
    @Autowired
    UserService userService;

    public RoleService createService(List<Role> roles) {
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        if(roleNames.contains("ROLE_ADMIN")){
            return adminService;
        }
        if(roleNames.contains("ROLE_MANAGER")){
            return managerService;
        }
        if(roleNames.contains("ROLE_USER")){
            return userService;
        }
        return null;
    }

}
