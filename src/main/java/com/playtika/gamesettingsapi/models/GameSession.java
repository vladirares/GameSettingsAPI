package com.playtika.gamesettingsapi.models;


import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "game_sessions")
public class GameSession {

    @EmbeddedId
    private GameSessionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameId")
    private Game game;

    @Column(name = "game_session_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime = new Date();

    @Column(name = "game_session_duration")
    private int duration;

    @Column(name = "game_session_is_time_exceeded")
    private boolean isTimeExceeded;

    public GameSessionId getId() {
        return id;
    }

    public void setId(GameSessionId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isTimeExceeded() {
        return isTimeExceeded;
    }

    public void setTimeExceeded(boolean timeExceeded) {
        isTimeExceeded = timeExceeded;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(user,game);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        GameSession that = (GameSession) obj;
//        return Objects.e
//    }
}