package com.roszczyk.pi4jexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("com.roszczyk.pi4jexample")
public class AppConfig {

    // Przykład Bean'a inicjowanego z konfiguracji
    @Bean
    public String welcomeMessage() {
        return "Demo aplikacji Spring, ze Schedulerem.";
    }
}
