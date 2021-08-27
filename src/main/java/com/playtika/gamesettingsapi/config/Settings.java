package com.playtika.gamesettingsapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Settings {

    @Value("${service.version:0.0.0}")
    private String version;

    @Value("${service.description}")
    private String description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}