package com.playtika.gamesettingsapi.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "game_name")
    private String name;

    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GameSession> gameSessions;

    public List<GameSession> getUsers() {
        return gameSessions;
    }

    public void setUsers(List<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

