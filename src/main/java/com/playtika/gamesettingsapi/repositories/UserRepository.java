package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    User findByUsername(String username);

    @Query(value = "SELECT * \n" +
            "\tFROM users \n" +
            "LEFT OUTER JOIN users_roles\n" +
            "\tON users.id = users_roles.user_id\n" +
            "INNER JOIN roles\n" +
            "\tON roles.id = users_roles.role_id\n" +
            "\tAND roles.name = :roleName ",nativeQuery = true)
    List<User> findByRoles(String roleName);

    @Query(value = "SELECT * \n" +
            "\tFROM users \n" +
            "LEFT OUTER JOIN users_roles\n" +
            "\tON users.id = users_roles.user_id\n" +
            "INNER JOIN roles\n" +
            "\tON roles.id = users_roles.role_id\n" +
            "\tAND (roles.name = \"ROLE_MANAGER\"OR\n" +
            "\troles.name = \"ROLE_USER\")",nativeQuery = true)
    List<User> findUsersByManager(Pageable pageable);

    @Query(value = "SELECT * FROM users",nativeQuery = true)
    List<User> findAllUsers(Pageable pageable);


    boolean existsByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
    @Override
    void delete(User user);
}
