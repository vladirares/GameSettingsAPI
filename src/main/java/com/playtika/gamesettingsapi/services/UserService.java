package com.playtika.gamesettingsapi.services;

import com.playtika.gamesettingsapi.dto.UserCRUDDTO;
import com.playtika.gamesettingsapi.exceptions.AuthenticationException;
import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.dto.LoginResponse;
import com.playtika.gamesettingsapi.security.dto.SignUpRequest;
import com.playtika.gamesettingsapi.security.dto.UserDTO;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import com.playtika.gamesettingsapi.security.services.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private RoleRepository roleRepository;


    //required by the UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(userName);

        if (user == null) {
            throw new UsernameNotFoundException("User '" + userName + "' not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(userName)
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }


    public LoginResponse login(String userName, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            User user = userRepository.findByUsername(userName);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setEmail(user.getEmail());
            loginResponse.setUserName(user.getUsername());
            loginResponse.setAccessToken(jwtTokenService.createToken(userName, user.getRoles()));

            logger.info("Login successfully");

            return loginResponse;
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User signUp(SignUpRequest request) {
        if(userRepository.existsByUsername(request.getUserName())){
            throw new AuthenticationException("User already exists in system", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if(request.getPassword() == null || request.getPassword().length() < 4){
            throw new AuthenticationException("Invalid password", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName(RoleType.ROLE_USER.name())));
        request.setPassword(user.getPassword());

        userRepository.save(user);
        logger.info("Register successfully");

        return user;
    }

    public User createUser(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new AuthenticationException("User already exists in system", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if(user.getPassword() == null || user.getPassword().length()<4){
            throw new AuthenticationException("Invalid password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return userRepository.saveAndFlush(user);
    }

    public void removeUser(String userName) {
        if(!userRepository.existsByUsername(userName)){
            throw new RuntimeException("User doesn't exists");
        }
        userRepository.deleteByUsername(userName);
        logger.info("User remove successfully");

    }

    public UserDTO searchUser(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new AuthenticationException("Provided user doesn't exist", HttpStatus.NOT_FOUND);
        }
        UserDTO userResponse = new UserDTO(user.getUsername(), user.getEmail());

        return userResponse;
    }

    public User getUser(String userName){
        return userRepository.findByUsername(userName);
    }

    public User setMaxPlayTime(UserCRUDDTO userCRUDDTO){
        User user = userRepository.findByUsername(userCRUDDTO.getUsername());
        user.setMaxPlaytime(userCRUDDTO.getMaxPlaytime());
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public String refreshToken(String userName) {
        return jwtTokenService.createToken(userName, userRepository.findByUsername(userName).getRoles());
    }


}