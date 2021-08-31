package com.playtika.gamesettingsapi.controllers;


import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.dto.LoginRequest;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.services.UserService;
import com.playtika.gamesettingsapi.services.factories.ManagerService;
import com.playtika.gamesettingsapi.services.factories.userCRUD.UserCRUDFactory;
import com.playtika.gamesettingsapi.specifications.UserSpecificationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ManagerService managerService;

    @Autowired
    UserCRUDFactory userCRUDFactory;

    @Autowired
    UserRepository userRepository;


    @GetMapping
    @RequestMapping("/login")
    public String login() {
        return "Login page";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest requestHeader, @RequestBody LoginRequest request) throws RuntimeException {

        LoginResponse loginResponse = userService.login(request.getUserName(), request.getPassword());
        if (loginResponse == null) {
            throw new RuntimeException("Login failed. Possible cause : incorrect username/password");
        } else {
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<User> signUp(HttpServletRequest requestHeader, @RequestBody SignUpRequest request) throws RuntimeException {

        User user;
        try {
            user = userService.signUp(request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) String search,
                                                  Authentication auth) {
        page = page == null ? 0 : page;
        size = size == null ? 3 : size;
        Pageable pageable = PageRequest.of(page,size);
        User user = userService.getUser(auth.getName());
        List<User> users = userCRUDFactory.createService(user.getRoles()).getAllUsers(pageable);
//        List<User> users = userRepository.findAll(spec);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/filtered/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsersFiltered(@RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String search,
                                                          Authentication auth) {
        //////////////////////////////////
        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)( eq | lt | gt )(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<User> spec = builder.build();
        //////////////////////////////////
        page = page == null ? 0 : page;
        size = size == null ? 3 : size;
        Pageable pageable = PageRequest.of(page,size);
        Page<User> pageData = userRepository.findAll(spec,pageable);
        List<User> users = pageData.getContent();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/users/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<User> createUser(@RequestBody UserCRUDDTO userDTO, Authentication auth) {
        User userCaller = userService.getUser(auth.getName());
        User result = userCRUDFactory.createService(userCaller.getRoles()).createUser(userDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/users/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<User> updateUser(@RequestBody UserCRUDDTO userDTO, Authentication auth) {
        User userCaller = userService.getUser(auth.getName());
        User result = userCRUDFactory.createService(userCaller.getRoles()).updateUser(userDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable long id, Authentication auth) throws RuntimeException {
        User userCaller = userService.getUser(auth.getName());
        boolean result = userCRUDFactory.createService(userCaller.getRoles()).deleteUser(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/maxdailyplaytime", params = {"time"})
    @PreAuthorize("hasRole('ROLE_ADMIN')" + "||hasRole('ROLE_USER')" + "||hasRole('ROLE_MANAGER')")
    public ResponseEntity<User> setMaxDailyPlayTime(@RequestParam int time, Authentication auth) throws RuntimeException {
        UserCRUDDTO userCRUDDTO = new UserCRUDDTO();
        userCRUDDTO.setUsername(auth.getName());
        userCRUDDTO.setMaxPlaytime(time);
        User response = userService.setMaxPlayTime(userCRUDDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<UserDTO> searchUser(@RequestParam String userName) throws RuntimeException {

        UserDTO userResponse = userService.searchUser(userName);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refreshToken(HttpServletRequest req) {
        return userService.refreshToken(req.getRemoteUser());
    }

//    @ExceptionHandler
//    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
//    public ResponseEntity<String> handleException(Exception e) {
//        return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
