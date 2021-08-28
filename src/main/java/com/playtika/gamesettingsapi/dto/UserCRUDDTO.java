package com.playtika.gamesettingsapi.dto;

import com.playtika.gamesettingsapi.security.models.Role;

import java.util.List;

public class UserCRUDDTO {
    private long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int maxPlaytime;
    private List<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMaxPlaytime() {
        return maxPlaytime;
    }

    public void setMaxPlaytime(int maxPlaytime) {
        this.maxPlaytime = maxPlaytime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
