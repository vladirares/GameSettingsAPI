package com.playtika.gamesettingsapi.controllers;

import com.playtika.gamesettingsapi.config.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/info")
public class SettingsController {

    @Autowired
    Settings settings;

    @GetMapping
    public Settings getSettings() {
        return settings;
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}