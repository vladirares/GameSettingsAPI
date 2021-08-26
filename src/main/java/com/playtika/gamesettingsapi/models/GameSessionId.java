package com.playtika.gamesettingsapi.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GameSessionId implements Serializable {

    @Column(name = "user_id")
    private long userId;

    @Column(name = "game_id")
    private long gameId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gameId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameSessionId that = (GameSessionId) obj;
        return userId == that.userId &&
                gameId == that.gameId;
    }

}
