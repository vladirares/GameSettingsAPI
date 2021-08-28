package com.playtika.gamesettingsapi.dto;

import com.playtika.gamesettingsapi.models.User;

import java.util.Date;

public class GameSessionDTO {
    private String gameName;
    private String userName;
    private int duration;
    private boolean isTimeExceeded;
    private Date startTime;
    private long id;
    private User user;

    public GameSessionDTO(String gameName, String userName, int duration, boolean isTimeExceeded, Date startTime, long id, User user) {
        this.gameName = gameName;
        this.userName = userName;
        this.duration = duration;
        this.isTimeExceeded = isTimeExceeded;
        this.startTime = startTime;
        this.id = id;
        this.user = user;
    }

    public GameSessionDTO(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
