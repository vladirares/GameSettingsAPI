package com.playtika.gamesettingsapi.controllers;


import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @PostMapping
    public User createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping
    public boolean deleteUser(@RequestBody long id) {
        return userService.deleteById(id);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateUser(@RequestBody User user) {
        if (userService.findById(user.getId()) == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }else{
            userService.updateUser(user);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
    }

}
