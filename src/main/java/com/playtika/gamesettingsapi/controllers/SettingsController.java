package com.playtika.gamesettingsapi.controllers;

import com.playtika.gamesettingsapi.config.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class SettingsController {

    @Autowired
    Settings settings;

    @GetMapping
    public Settings getSettings() {
        return settings;
    }


}