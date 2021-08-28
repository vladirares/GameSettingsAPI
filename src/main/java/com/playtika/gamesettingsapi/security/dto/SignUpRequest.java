package com.playtika.gamesettingsapi.security.dto;

import com.playtika.gamesettingsapi.security.models.Role;

import java.util.List;

public class SignUpRequest {
    private String userName;
    private String email;
    private String password;
//    private List<Role> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public List<Role> getRoles() {
//        return roles;
//    }

//    public void setRoles(List<Role> roles) {
//        this.roles = roles;
//    }
}
