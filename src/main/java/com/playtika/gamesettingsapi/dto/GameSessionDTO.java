package com.playtika.gamesettingsapi.dto;

import java.util.Date;

public class GameSessionDTO {
    private String gameName;
    private int duration;
    private boolean isTimeExceeded;
    private Date startTime;

    public GameSessionDTO(String gameName, int duration, boolean isTimeExceeded, Date startTime) {
        this.gameName = gameName;
        this.duration = duration;
        this.isTimeExceeded = isTimeExceeded;
        this.startTime = startTime;
    }

    public GameSessionDTO(){

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
