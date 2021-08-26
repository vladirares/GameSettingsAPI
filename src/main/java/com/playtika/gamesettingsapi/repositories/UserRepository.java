package com.playtika.gamesettingsapi.repositories;

import com.playtika.gamesettingsapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
