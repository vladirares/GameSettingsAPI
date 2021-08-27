package com.playtika.gamesettingsapi.security.config;

import com.playtika.gamesettingsapi.models.User;
import com.playtika.gamesettingsapi.repositories.UserRepository;
import com.playtika.gamesettingsapi.security.models.Role;
import com.playtika.gamesettingsapi.security.models.RoleType;
import com.playtika.gamesettingsapi.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SetupRoles implements ApplicationListener<ContextRefreshedEvent> {

    boolean setupComplete = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(setupComplete)
            return;

        // == create initial roles
        final Role adminRole = createRoleIfNotFound(RoleType.ROLE_ADMIN.name());
        createRoleIfNotFound(RoleType.ROLE_USER.name());
        createRoleIfNotFound(RoleType.ROLE_MANAGER.name());

        // == create initial user
        createUserIfNotFound("admin@test.com", "admin", "Admin",
                "Admin", "1234", new ArrayList<>(Arrays.asList(adminRole)));

        setupComplete = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String username, final String firstName, final String lastName, final String password, final List<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }
}